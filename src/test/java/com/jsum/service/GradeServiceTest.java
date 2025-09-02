package com.jsum.service;

import com.jsum.exception.EnrollmentNotFoundException;
import com.jsum.exception.InvalidGradeException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GradeServiceTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("h2-test");

    @Test
    void addGrade_notValid() {
        QueryTestService queryTestService = new QueryTestService(emf);
        StudentEnrollmentService studentEnrollmentService = new StudentEnrollmentService(emf);
        GradeService gradeService = new GradeService(emf);

        Long sid = queryTestService.addStudent("Zahra", "zahra@u.test", "CS");
        Long cid = queryTestService.addCourse("Linear Algebra", 3);

        assertThrows(
                EnrollmentNotFoundException.class,
                () -> gradeService.saveNumeric(sid, cid, "Fall 2025", 15.0)
        );

        studentEnrollmentService.enroll(sid, cid, "Fall 2025");

        assertThrows(
                InvalidGradeException.class,
                () -> gradeService.saveNumeric(sid, cid, "Fall 2025", -5.0)
        );

        assertDoesNotThrow(() -> gradeService.saveNumeric(sid, cid, "Fall 2025", 13.0));
    }

}
