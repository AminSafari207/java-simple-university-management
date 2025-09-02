package com.jsum.service;

import com.jsum.enums.GradeType;
import com.jsum.exception.EnrollmentNotFoundException;
import com.jsum.exception.InvalidGradeException;
import com.jsum.model.Course;
import com.jsum.model.Grade;
import com.jsum.repository.impl.CourseRepositoryImpl;
import com.jsum.repository.impl.GradeRepositoryImpl;
import com.jsum.repository.impl.StudentEnrollmentRepositoryImpl;
import com.jsum.service.base.TransactionalService;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public class GradeService extends TransactionalService {
    private double numericThreshold = 10.0;
    private String minLetter = "C-";

    public GradeService(EntityManagerFactory emf) {
        super(emf);
    }

    public void setNumericThreshold(double threshold) {
        this.numericThreshold = threshold;
    }

    public void setMinLetter(String minLetter) {
        this.minLetter = minLetter;
    }

    public Grade saveNumeric(Long studentId, Long courseId, String semester, double value) {
        return executeTransaction(em -> {
            if (value < 0.0 || value > 20.0) {
                throw new InvalidGradeException("Numeric grade is out of range: " + value);
            }

            GradeRepositoryImpl gradeRepository = new GradeRepositoryImpl(em);
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);
            StudentEnrollmentRepositoryImpl studentEnrollmentRepository = new StudentEnrollmentRepositoryImpl(em);

            gradeRepository.findUnique(studentId, courseId, semester).ifPresent(g -> {
                throw new RuntimeException("Duplicate grade");
            });

            if (!studentEnrollmentRepository.exists(studentId, courseId, semester)) {
                throw new EnrollmentNotFoundException("Student not enrolled in this course/semester");
            }

            Course course = courseRepository.findById(courseId).orElseThrow();

            Grade grade = new Grade()
                .studentId(studentId)
                .courseId(course.getId())
                .semester(semester)
                .gradeType(GradeType.NUMERIC)
                .numericValue(value);

            return gradeRepository.save(grade);
        });
    }

    public Grade saveLetter(Long studentId, Long courseId, String semester, String letter) {
        return executeTransaction(em -> {
            GradeRepositoryImpl gradeRepository = new GradeRepositoryImpl(em);
            StudentEnrollmentRepositoryImpl studentEnrollmentRepository = new StudentEnrollmentRepositoryImpl(em);

            gradeRepository.findUnique(studentId, courseId, semester).ifPresent(g -> {
                throw new RuntimeException("Duplicate grade");
            });

            if (!studentEnrollmentRepository.exists(studentId, courseId, semester)) {
                throw new EnrollmentNotFoundException("Student not enrolled in this course/semester");
            }

            Grade grade = new Grade()
                    .studentId(studentId)
                    .courseId(courseId)
                    .semester(semester)
                    .gradeType(GradeType.LETTER)
                    .letterValue(letter);

            return gradeRepository.save(grade);
        });
    }

    public boolean isPassing(Grade grade) {
        GradeType gradeType = grade.getGradeType();

        if (gradeType == GradeType.NUMERIC) return Optional.ofNullable(grade.getNumericValue()).orElse(0.0) >= numericThreshold;
        if (gradeType == GradeType.LETTER) return letterRank(Optional.ofNullable(grade.getLetterValue()).orElse("F")) >= letterRank(minLetter);

        return false;
    }

    public double calculateStudentGpa(Long studentId) {
        return executeTransaction(em -> {
            GradeRepositoryImpl gradeRepository = new GradeRepositoryImpl(em);
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);

            List<Grade> grades = gradeRepository.findByStudent(studentId);

            if (grades.isEmpty()) return 0.0D;

            double totalWeighted = 0.0D;
            int totalCredits = 0;

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

            return totalCredits == 0 ? 0.0 : totalWeighted / totalCredits;
        });
    }

    private int letterRank(String letter) {
        return switch (letter.toUpperCase()) {
            case "A" -> 12;
            case "A-" -> 11;
            case "B+" -> 10;
            case "B" -> 9;
            case "B-" -> 8;
            case "C+" -> 7;
            case "C" -> 6;
            case "C-" -> 5;
            case "D+" -> 4;
            case "D" -> 3;
            case "D-" -> 2;
            default -> 1;
        };
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