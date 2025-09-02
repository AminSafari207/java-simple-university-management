package repository.impl;

import com.jsum.model.person.Professor;
import jakarta.persistence.EntityManager;
import repository.base.BaseRepository;

import java.util.List;

public class ProfessorRepositoryImpl extends BaseRepository<Professor, Long> {
    public ProfessorRepositoryImpl(EntityManager em) {
        super(em, Professor.class);
    }

    public List<Professor> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        return em.createQuery("select p from " + classRef.getSimpleName() + " p where p.id in :ids", classRef)
                .setParameter("ids", ids)
                .getResultList();
    }
}
