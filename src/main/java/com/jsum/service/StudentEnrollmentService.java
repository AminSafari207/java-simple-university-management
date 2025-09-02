package com.jsum.service;

import com.jsum.exception.DuplicateEnrollmentException;
import com.jsum.model.Course;
import com.jsum.model.StudentEnrollment;
import com.jsum.model.person.Student;
import com.jsum.repository.impl.CourseRepositoryImpl;
import com.jsum.repository.impl.StudentEnrollmentRepositoryImpl;
import com.jsum.repository.impl.StudentRepositoryImpl;
import com.jsum.service.base.TransactionalService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

public class StudentEnrollmentService extends TransactionalService {
    public StudentEnrollmentService(EntityManagerFactory emf) {
        super(emf);
    }

    public void enroll(Long studentId, Long courseId, String semester) {
        executeTransactionVoid(em -> {
            StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(em);
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);
            StudentEnrollmentRepositoryImpl studentEnrollmentRepository = new StudentEnrollmentRepositoryImpl(em);

            Student student = studentRepository.findById(studentId).orElseThrow(
                    () -> new EntityNotFoundException("Student not found: " + studentId)
            );
            Course course = courseRepository.findById(courseId).orElseThrow(
                    () -> new EntityNotFoundException("Course not found: " + courseId)
            );

            if (studentEnrollmentRepository.exists(studentId, courseId, semester)) {
                throw new DuplicateEnrollmentException("Duplicate student enrollment.");
            }

            StudentEnrollment enrollment = new StudentEnrollment();
            enrollment.studentId(student.getId())
                    .courseId(course.getId())
                    .semester(semester);

            studentEnrollmentRepository.save(enrollment);
        });
    }

    public void removeEnroll(Long studentId, Long courseId, String semester) {
        executeTransactionVoid(em -> {
            StudentEnrollmentRepositoryImpl studentEnrollmentRepository = new StudentEnrollmentRepositoryImpl(em);

            StudentEnrollment enrollment = studentEnrollmentRepository.findUnique(studentId, courseId, semester)
                    .orElseThrow(() -> new RuntimeException("Not enrolled"));

            try {
                studentEnrollmentRepository.deleteById(enrollment.getId());
            } catch (Exception e) {
                throw new RuntimeException("Removing student enrollment failed.", e);
            }
        });
    }

    public boolean isActive(Long studentId, String semester) {
        return executeTransaction(em -> {
            StudentEnrollmentRepositoryImpl studentEnrollmentRepository = new StudentEnrollmentRepositoryImpl(em);

            return !studentEnrollmentRepository.findByStudentAndSemester(studentId, semester).isEmpty();
        });
    }

    public List<Course> listEnrolledCourses(Long studentId) {
        return executeTransaction(em -> {
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);

            return courseRepository.findByStudentId(studentId);
        });
    }
}
