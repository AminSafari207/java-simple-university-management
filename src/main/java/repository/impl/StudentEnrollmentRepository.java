package repository.impl;

import com.jsum.model.StudentEnrollment;
import jakarta.persistence.EntityManager;
import repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

public class StudentEnrollmentRepository extends BaseRepository<StudentEnrollment, Long> {
    public StudentEnrollmentRepository(EntityManager em) {
        super(em, StudentEnrollment.class);
    }

    public Optional<StudentEnrollment> findUnique(Long studentId, Long courseId, String semester) {
        return em.createQuery("select e from " + classRef.getSimpleName() +
                        " e where e.studentId=:sid and e.courseId=:cid and e.semester=:sem", classRef)
                .setParameter("sid", studentId)
                .setParameter("cid", courseId)
                .setParameter("sem", semester)
                .getResultStream()
                .findFirst();
    }

    public List<StudentEnrollment> findByStudent(Long studentId) {
        return em.createQuery("select e from " + classRef.getSimpleName() + " e where e.studentId=:sid", classRef)
                .setParameter("sid", studentId)
                .getResultList();
    }

    public List<StudentEnrollment> findByStudentAndSemester(Long studentId, String semester) {
        return em.createQuery("select e from " + classRef.getSimpleName() + " e where e.studentId=:sid and e.semester=:sem", classRef)
                .setParameter("sid", studentId)
                .setParameter("sem", semester)
                .getResultList();
    }

    public List<StudentEnrollment> findByCourse(Long courseId) {
        return em.createQuery("select e from " + classRef.getSimpleName() + " e where e.courseId=:cid", classRef)
                .setParameter("cid", courseId)
                .getResultList();
    }
}
