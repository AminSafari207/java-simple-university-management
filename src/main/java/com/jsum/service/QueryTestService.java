package com.jsum.service;

import com.jsum.enums.GradeType;
import com.jsum.model.Grade;
import com.jsum.model.ProfessorEnrollment;
import com.jsum.model.person.Professor;
import com.jsum.model.person.Student;
import com.jsum.repository.impl.GradeRepositoryImpl;
import com.jsum.repository.impl.ProfessorEnrollmentRepositoryImpl;
import com.jsum.repository.impl.ProfessorRepositoryImpl;
import com.jsum.repository.impl.StudentRepositoryImpl;
import com.jsum.service.base.TransactionalService;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryTestService extends TransactionalService {
    public QueryTestService(EntityManagerFactory emf) {
        super(emf);
    }

    public List<Student> getStudentsWithFails() {
        return executeTransaction(entityManager -> {
            StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(entityManager);
            GradeRepositoryImpl gradeRepository = new GradeRepositoryImpl(entityManager);

            Map<Long, List<Grade>> gradesByStudent = gradeRepository.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(Grade::getStudentId));

            return studentRepository.findAll()
                    .stream()
                    .filter(
                            student -> gradesByStudent.getOrDefault(student.getId(), List.of())
                                    .stream()
                                    .anyMatch(grade -> !checkPassing(grade))
                    )
                    .toList();
        });
    }

    public List<Professor> getProfessorsEnrollmentsMoreThan(int n) {
        return executeTransaction(entityManager -> {
            ProfessorRepositoryImpl professorRepository = new ProfessorRepositoryImpl(entityManager);
            ProfessorEnrollmentRepositoryImpl professorEnrollmentRepository = new ProfessorEnrollmentRepositoryImpl(entityManager);

            Map<Long, Long> counts = professorEnrollmentRepository.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(ProfessorEnrollment::getProfessorId, Collectors.counting()));

            return professorRepository.findAll().stream()
                    .filter(professor -> counts.getOrDefault(professor.getId(), 0L) > n)
                    .toList();
        });
    }

    public double averageGpaAllStudents() {
        GradeService gradeService = new GradeService(emf);

        return executeTransaction(entityManager -> {
            StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(entityManager);

            List<Student> students = studentRepository.findAll();

            if (students.isEmpty()) return 0.0D;

            double sum = 0.0D;

            for (Student student : students) {
                sum += gradeService.calculateStudentGpa(student.getId());
            }

            return sum / students.size();
        });
    }

    private boolean checkPassing(Grade grade) {
        if (grade.getGradeType() == GradeType.NUMERIC) {
            double v = grade.getNumericValue() == null ? 0.0 : grade.getNumericValue();

            return v >= 10.0;
        } else {
            return letterRank(grade.getLetterValue() == null ? "F" : grade.getLetterValue()) >= letterRank("C-");
        }
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

}
