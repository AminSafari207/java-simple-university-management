package repository.impl;

import com.jsum.model.ProfessorEnrollment;
import jakarta.persistence.EntityManager;
import repository.base.BaseRepository;

import java.util.List;

public class ProfessorEnrollmentRepositoryImpl extends BaseRepository<ProfessorEnrollment, Long> {
    public ProfessorEnrollmentRepositoryImpl(EntityManager em) {
        super(em, ProfessorEnrollment.class);
    }

    public long countCoursesForProfessor(Long profId) {
        return em.createQuery("select count(t) from com.jsum.model.ProfessorEnrollment t where t.professorId=:pid", Long.class)
                .setParameter("pid", profId)
                .getSingleResult();
    }

    public List<ProfessorEnrollment> findByCourse(Long courseId) {
        return em.createQuery("select t from " + classRef.getSimpleName() + " t where t.courseId=:cid", classRef)
                .setParameter("cid", courseId)
                .getResultList();
    }

    public List<ProfessorEnrollment> findByProfessor(Long profId) {
        return em.createQuery("select t from " + classRef.getSimpleName() + " t where t.professorId=:pid", classRef)
                .setParameter("pid", profId)
                .getResultList();
    }

    public List<Long> professorIdsForCourse(Long courseId) {
        return em.createQuery("select t.professorId from com.jsum.model.ProfessorEnrollment t where t.courseId=:cid", Long.class)
                .setParameter("cid", courseId)
                .getResultList();
    }

    public List<Long> courseIdsForProfessor(Long professorId) {
        return em.createQuery("select t.courseId from com.jsum.model.ProfessorEnrollment t where t.professorId=:pid", Long.class)
                .setParameter("pid", professorId)
                .getResultList();
    }


}
