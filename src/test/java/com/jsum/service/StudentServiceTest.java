package com.jsum.service;

import com.jsum.exception.DuplicateEmailException;
import com.jsum.exception.DuplicateEnrollmentException;
import com.jsum.exception.InvalidGradeException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("h2-test");

    @Test
    void addStudent_duplicateEmailException() {
        QueryTestService queryTestService = new QueryTestService(emf);

        Long s1 = queryTestService.addStudent("Sara", "sara@u.test", "SE");
        assertNotNull(s1);

        assertThrows(
                DuplicateEmailException.class,
                () -> queryTestService.addStudent("Sara 2", "sara@u.test", "SE")
        );
    }

    @Test
    void enrollStudent_CustomExceptions() {
        QueryTestService queryTestService = new QueryTestService(emf);
        StudentEnrollmentService studentEnrollmentService = new StudentEnrollmentService(emf);

        Long sid = queryTestService.addStudent("Ali", "ali@u.test", "CS");
        Long cid = queryTestService.addCourse("Algorithms", 4);

        assertDoesNotThrow(() -> studentEnrollmentService.enroll(sid, cid, "Fall 2025"));

        assertThrows(DuplicateEnrollmentException.class,
                () -> studentEnrollmentService.enroll(sid, cid, "Fall 2025"));

        assertThrows(EntityNotFoundException.class,
                () -> studentEnrollmentService.enroll(99999L, cid, "Fall 2025"));

        assertThrows(EntityNotFoundException.class,
                () -> studentEnrollmentService.enroll(sid, 88888L, "Fall 2025"));
    }

    @Test
    void calculateGpa_noGradeShouldBeZero_AND_invalidValuesShouldThrowException() {
        QueryTestService queryTestService = new QueryTestService(emf);
        StudentEnrollmentService studentEnrollmentService = new StudentEnrollmentService(emf);
        GradeService gradeService = new GradeService(emf);
        StudentService studentService = new StudentService(emf);

        Long sid = queryTestService.addStudent("Neda", "neda@u.test", "CS");
        Long c1 = queryTestService.addCourse("DB", 3);
        Long c2 = queryTestService.addCourse("OS", 4);

        assertEquals(0.0, gradeService.calculateStudentGpa(sid), 0.0001);

        studentEnrollmentService.enroll(sid, c1, "Fall 2025");
        studentEnrollmentService.enroll(sid, c2, "Fall 2025");

        gradeService.saveNumeric(sid, c1, "Fall 2025", 20.0);
        gradeService.saveNumeric(sid, c2, "Fall 2025", 10.0);

        double gpa = studentService.getStudentWithComputed(sid, "Fall 2025").getGpa();

        double expected = ((20.0 / 20.0) * 4.0 * 3 + (10.0 / 20.0) * 4.0 * 4) / (3 + 4);

        assertEquals(expected, gpa, 0.0001);

        assertThrows(InvalidGradeException.class,
                () -> gradeService.saveNumeric(sid, c1, "Fall 2025", -1.0));

        assertThrows(InvalidGradeException.class,
                () -> gradeService.saveNumeric(sid, c1, "Fall 2025", 21.0));

    }
}
