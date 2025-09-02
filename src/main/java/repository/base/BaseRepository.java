package repository.base;

import com.jsum.model.base.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T, ID> extends BaseEntity<ID> implements CrudRepository<T, ID> {
    protected final EntityManager em;
    protected final Class<T> classRef;

    public BaseRepository(EntityManager em, Class<T> classRef) {
        this.em = em;
        this.classRef = classRef;
    }

    @Override
    public T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(classRef, id));
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("select e from " + classRef.getSimpleName() + " e", classRef).getResultList();

    }

    @Override
    public boolean deleteById(ID id) throws Exception {
        try {
            T ref = em.getReference(classRef, id);
            em.remove(ref);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    protected Class<T> getClassRef() {
        return classRef;
    }
}
