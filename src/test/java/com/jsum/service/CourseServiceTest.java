package com.jsum.service;

import com.jsum.exception.DuplicateCourseNameException;
import com.jsum.exception.TooManyCoursesException;
import com.jsum.model.person.Professor;
import com.jsum.model.person.Student;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseServiceTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("h2-test");

    @Test
    void assignCourseToProfessor_PLUS_preventDuplicateCourseName_CustomExceptions() {
        QueryTestService queryTestService = new QueryTestService(emf);
        CourseService courseService = new CourseService(emf);

        Long pid = queryTestService.addProfessor("Dr X", "x@u.test", 100_000.0);

        Long a = courseService.addCourse("A", 3);
        Long b = courseService.addCourse("B", 3);
        Long c = courseService.addCourse("C", 3);
        Long d = courseService.addCourse("D", 3);

        assertThrows(
                EntityNotFoundException.class,
                () -> courseService.assignCoursesToProfessor(pid, List.of(99999L))
        );

        Long e = courseService.addCourse("E", 3);
        Long f = courseService.addCourse("F", 3);

        assertThrows(
                DuplicateCourseNameException.class,
                () -> courseService.addCourse("A", 3)
        );

        courseService.assignCoursesToProfessor(pid, List.of(a, b, c, d, e));

        assertThrows(
                TooManyCoursesException.class,
                () -> courseService.assignCoursesToProfessor(pid, List.of(f))
        );
    }

    @Test
    void findProfessorsWithCoursesMoreThanN_CustomException() {
        QueryTestService queryTestService = new QueryTestService(emf);
        ProfessorEnrollmentService professorEnrollmentService = new ProfessorEnrollmentService(emf);
        StudentEnrollmentService studentEnrollmentService = new StudentEnrollmentService(emf);
        GradeService gradeService = new GradeService(emf);

        Long p = queryTestService.addProfessor("Dr Y", "y@u.test", 120_000.0);

        Long c1 = queryTestService.addCourse("G1", 3);
        Long c2 = queryTestService.addCourse("G2", 3);
        Long c3 = queryTestService.addCourse("G3", 3);
        Long c4 = queryTestService.addCourse("G4", 3);

        professorEnrollmentService.enrollProfessor(c1, p);
        professorEnrollmentService.enrollProfessor(c2, p);
        professorEnrollmentService.enrollProfessor(c3, p);
        professorEnrollmentService.enrollProfessor(c4, p);

        List<Professor> moreThan3 = queryTestService.getProfessorsEnrollmentsMoreThan(3);

        assertTrue(moreThan3.stream().anyMatch(pr -> pr.getEmail().equals("y@u.test")));

        Long s1 = queryTestService.addStudent("Sara", "s1@u.test", "SE");
        Long s2 = queryTestService.addStudent("Hamed", "s2@u.test", "IT");

        studentEnrollmentService.enroll(s1, c1, "Fall 2025");
        studentEnrollmentService.enroll(s2, c1, "Fall 2025");

        gradeService.saveNumeric(s1, c1, "Fall 2025", 9.0);
        gradeService.saveNumeric(s2, c1, "Fall 2025", 18.0);

        List<Student> failing = queryTestService.getStudentsWithFails();

        assertTrue(failing.stream().anyMatch(st -> st.getEmail().equals("s1@u.test")));
        assertFalse(failing.stream().anyMatch(st -> st.getEmail().equals("s2@u.test")));
    }
}
