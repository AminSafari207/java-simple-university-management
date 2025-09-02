package repository.impl;

import com.jsum.model.Course;
import jakarta.persistence.EntityManager;
import repository.base.BaseRepository;

import java.util.List;

public class CourseRepositoryImpl extends BaseRepository<Course, Long> {
    public CourseRepositoryImpl(EntityManager em) {
        super(em, Course.class);
    }

    public List<Course> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return em.createQuery("select c from " + classRef.getSimpleName() + " c where c.id in :ids", classRef)
                .setParameter("ids", ids)
                .getResultList();
    }

    public List<Course> findByStudentId(Long studentId) {
        return em.createQuery("select c from Course c where c.id in (select e.courseId from com.jsum.model.StudentEnrollment e where e.studentId=:sid)", Course.class)
                .setParameter("sid", studentId)
                .getResultList();
    }

    public List<Course> findByProfessorId(Long professorId) {
        return em.createQuery("select c from Course c where c.id in (select t.courseId from com.jsum.model.ProfessorEnrollment t where t.professorId=:pid)", Course.class)
                .setParameter("pid", professorId)
                .getResultList();
    }
}
