package com.jsum.service;

import com.jsum.model.Course;
import com.jsum.model.ProfessorEnrollment;
import com.jsum.model.person.Professor;
import com.jsum.repository.impl.CourseRepositoryImpl;
import com.jsum.repository.impl.ProfessorEnrollmentRepositoryImpl;
import com.jsum.repository.impl.ProfessorRepositoryImpl;
import com.jsum.service.base.TransactionalService;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ProfessorEnrollmentService extends TransactionalService {
    public ProfessorEnrollmentService(EntityManagerFactory emf) {
        super(emf);
    }

    public void enrollProfessor(Long courseId, Long professorId) {
        executeTransactionVoid(em -> {
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);
            ProfessorRepositoryImpl professorRepository = new ProfessorRepositoryImpl(em);
            ProfessorEnrollmentRepositoryImpl professorEnrollmentRepository = new ProfessorEnrollmentRepositoryImpl(em);

            courseRepository.findById(courseId).orElseThrow();
            professorRepository.findById(professorId).orElseThrow();

            boolean exists = professorEnrollmentRepository.findByCourse(courseId)
                    .stream()
                    .anyMatch(ta -> ta.getProfessorId().equals(professorId));

            if (exists) {
                throw new RuntimeException("Duplicate professor enrollment.");
            }

            ProfessorEnrollment professorEnrollment = new ProfessorEnrollment();
            professorEnrollment.courseId(courseId)
                    .professorId(professorId);

            professorEnrollmentRepository.save(professorEnrollment);
        });
    }

    public void removeProfessorEnrollment(Long courseId, Long professorId) {
        executeTransactionVoid(em -> {
            ProfessorEnrollmentRepositoryImpl professorEnrollmentRepository = new ProfessorEnrollmentRepositoryImpl(em);

            ProfessorEnrollment professorEnrollment = professorEnrollmentRepository.findByCourse(courseId)
                    .stream()
                    .filter(x -> x.getProfessorId().equals(professorId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Professor not enrolled."));

            try {
                professorEnrollmentRepository.deleteById(professorEnrollment.getId());
            } catch (Exception e) {
                throw new RuntimeException("Removing professor enrollment failed.", e);
            }
        });
    }

    public List<Professor> getProfessorsForCourse(Long courseId) {
        return executeTransaction(em -> {
            ProfessorEnrollmentRepositoryImpl professorEnrollmentRepository = new ProfessorEnrollmentRepositoryImpl(em);
            ProfessorRepositoryImpl professorRepository = new ProfessorRepositoryImpl(em);

            List<Long> ids = professorEnrollmentRepository.professorIdsForCourse(courseId);

            return professorRepository.findByIds(ids);
        });
    }

    public List<Course> getCoursesForProfessor(Long professorId) {
        return executeTransaction(em -> {
            ProfessorEnrollmentRepositoryImpl professorEnrollmentRepository = new ProfessorEnrollmentRepositoryImpl(em);
            CourseRepositoryImpl courseRepository = new CourseRepositoryImpl(em);

            List<Long> ids = professorEnrollmentRepository.courseIdsForProfessor(professorId);

            return courseRepository.findByIds(ids);
        });
    }
}
