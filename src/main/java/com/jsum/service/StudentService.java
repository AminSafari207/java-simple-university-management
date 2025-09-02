package com.jsum.service;

import com.jsum.enums.GradeType;
import com.jsum.model.Course;
import com.jsum.model.Grade;
import com.jsum.model.person.Student;
import com.jsum.repository.impl.CourseRepositoryImpl;
import com.jsum.repository.impl.GradeRepositoryImpl;
import com.jsum.repository.impl.StudentEnrollmentRepositoryImpl;
import com.jsum.repository.impl.StudentRepositoryImpl;
import com.jsum.service.base.TransactionalService;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public class StudentService extends TransactionalService {
    public StudentService(EntityManagerFactory emf) {
        super(emf);
    }

    public Student getStudentById(Long studentId) {
        return executeTransaction(em -> {
            StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(em);
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);

            Student student = studentRepository.findById(studentId).orElseThrow();
            List<Course> courses = courseRepository.findByStudentId(studentId);

            student.enrolledCourses(courses);

            return student;
        });
    }

    public Student getStudentWithComputed(Long studentId, String semester) {
        return executeTransaction(em -> {
            StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(em);
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);
            StudentEnrollmentRepositoryImpl studentEnrollmentRepository = new StudentEnrollmentRepositoryImpl(em);
            GradeRepositoryImpl gradeRepository = new GradeRepositoryImpl(em);

            Student student = studentRepository.findById(studentId).orElseThrow();

            List<Course> courses = courseRepository.findByStudentId(studentId);

            student.enrolledCourses(courses);

            boolean active = !studentEnrollmentRepository.findByStudentAndSemester(studentId, semester).isEmpty();

            student.isActive(active);

            double totalWeighted = 0.0D;
            int totalCredits = 0;

            List<Grade> grades = gradeRepository.findByStudent(studentId);

            for (Grade grade : grades) {
                Course course = courseRepository.findById(grade.getCourseId()).orElseThrow();
                int credits = course.getCredits();

                double gpa = 0D;
                GradeType gradeType = grade.getGradeType();

                if (gradeType == GradeType.NUMERIC) gpa = (Optional.ofNullable(grade.getNumericValue()).orElse(0.0) / 20.0) * 4.0;
                if (gradeType == GradeType.LETTER) gpa = letterToGpa(Optional.ofNullable(grade.getLetterValue()).orElse("F"));

                totalWeighted += gpa * credits;
                totalCredits += credits;
            }

            double gpa = totalCredits == 0 ? 0.0 : totalWeighted / totalCredits;

            student.gpa(gpa);

            return student;
        });
    }

    private double letterToGpa(String letter) {
        return switch (letter.toUpperCase()) {
            case "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "C-" -> 1.7;
            case "D+" -> 1.3;
            case "D" -> 1.0;
            case "D-" -> 0.7;
            default -> 0.0;
        };
    }
}


