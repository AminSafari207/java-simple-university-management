package repository.impl;

import com.jsum.model.Grade;
import jakarta.persistence.EntityManager;
import repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

public class GradeRepositoryImpl extends BaseRepository<Grade, Long> {
    public GradeRepositoryImpl(EntityManager em) {
        super(em, Grade.class);
    }

    public Optional<Grade> findUnique(Long studentId, Long courseId, String semester) {
        return em.createQuery("select g from " + classRef.getSimpleName() +
                        " g where g.studentId=:sid and g.courseId=:cid and g.semester=:sem", classRef)
                .setParameter("sid", studentId)
                .setParameter("cid", courseId)
                .setParameter("sem", semester)
                .getResultStream()
                .findFirst();
    }

    public List<Grade> findByStudent(Long studentId) {
        return em.createQuery("select g from " + classRef.getSimpleName() + " g where g.studentId=:sid", classRef)
                .setParameter("sid", studentId)
                .getResultList();
    }

    public List<Grade> findByStudentAndSemester(Long studentId, String semester) {
        return em.createQuery("select g from " + classRef.getSimpleName() + " g where g.studentId=:sid and g.semester=:sem", classRef)
                .setParameter("sid", studentId)
                .setParameter("sem", semester)
                .getResultList();
    }
}
