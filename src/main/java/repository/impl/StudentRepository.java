package repository.impl;

import com.jsum.model.person.Student;
import jakarta.persistence.EntityManager;
import repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

public class StudentRepository extends BaseRepository<Student, Long> {

    public StudentRepository(EntityManager em) {
        super(em, Student.class);
    }

    public List<Student> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        return em.createQuery("select s from " + classRef.getSimpleName() + " s where s.id in :ids", classRef)
                .setParameter("ids", ids)
                .getResultList();
    }

    public Optional<Student> findByEmail(String email) {
        return em.createQuery("select s from " + classRef.getSimpleName() + " s where s.email=:e", classRef)
                .setParameter("e", email)
                .getResultStream()
                .findFirst();
    }
}
