package com.jsum.service;

import com.jsum.exception.DuplicateCourseNameException;
import com.jsum.exception.TooManyCoursesException;
import com.jsum.model.Course;
import com.jsum.model.ProfessorEnrollment;
import com.jsum.model.person.Professor;
import com.jsum.repository.impl.CourseRepositoryImpl;
import com.jsum.repository.impl.ProfessorEnrollmentRepositoryImpl;
import com.jsum.repository.impl.ProfessorRepositoryImpl;
import com.jsum.service.base.TransactionalService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseService extends TransactionalService {
    private final int maxCoursesPerProfessor = 5;

    public CourseService(EntityManagerFactory emf) {
        super(emf);
    }

    public Long addCourse(String name, int credits) {
        return executeTransaction(entityManager -> {
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(entityManager);

            Course existing = courseRepository.findByName(name);
            if (existing != null) {
                throw new DuplicateCourseNameException("Course name already exists: " + name);
            }

            Course course = new Course().name(name).credits(credits);
            course = courseRepository.save(course);
            return course.getId();
        });
    }

    public void assignCoursesToProfessor(Long professorId, List<Long> courseIds) {
        executeTransactionVoid(entityManager -> {
            ProfessorRepositoryImpl professorRepository = new ProfessorRepositoryImpl(entityManager);
            ProfessorEnrollmentRepositoryImpl professorEnrollmentRepository = new ProfessorEnrollmentRepositoryImpl(entityManager);
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(entityManager);

            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new EntityNotFoundException("Professor not found"));

            long currentCount = professorEnrollmentRepository.countCoursesForProfessor(professor.getId());

            Set<Long> uniqueCourseIds = new HashSet<>(courseIds);

            long total = currentCount + uniqueCourseIds.size();

            if (total > maxCoursesPerProfessor) {
                throw new TooManyCoursesException("Professor must have " + total + " courses (max " + maxCoursesPerProfessor + ")");
            }

            for (Long courseId : uniqueCourseIds) {
                courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
            }

            for (Long courseId : uniqueCourseIds) {
                List<Long> profIds = professorEnrollmentRepository.professorIdsForCourse(courseId);

                boolean alreadyAssigned = profIds.stream().anyMatch(id -> id.equals(professorId));

                if (!alreadyAssigned) {
                    ProfessorEnrollment professorEnrollment = new ProfessorEnrollment()
                            .courseId(courseId)
                            .professorId(professorId);

                    professorEnrollmentRepository.save(professorEnrollment);
                }
            }
        });
    }

    public List<Professor> findProfessorsTeachingMoreThan(int n) {
        return executeTransaction(entityManager -> {
            ProfessorEnrollmentRepositoryImpl professorEnrollmentRepository = new ProfessorEnrollmentRepositoryImpl(entityManager);
            ProfessorRepositoryImpl professorRepository = new ProfessorRepositoryImpl(entityManager);

            Map<Long, Long> counts = professorEnrollmentRepository.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(
                            ProfessorEnrollment::getProfessorId,
                            Collectors.counting()
                    ));

            return professorRepository.findAll().stream()
                    .filter(p -> counts.getOrDefault(p.getId(), 0L) > n)
                    .toList();
        });
    }
}
