package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA implementation of the DAO for Jwt.
 */
@Repository
public class JpaJwtDao implements Dao<Jwt>{

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Optional<Jwt> get(Integer id) {
        return Optional.ofNullable(entityManager.find(Jwt.class, id));
    }

    @Override
    public List<Jwt> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Jwt e");
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(Jwt jwt) {
        entityManager.persist(jwt);
    }

    @Transactional
    @Override
    public void update(Jwt jwt, String[] params) {
        Objects.requireNonNull(params, "Params cannot be null");
        Objects.requireNonNull(params[0], "desactive cannot be null");
        Objects.requireNonNull(params[1], "expire cannot be null");

        jwt.setDesactive(Boolean.parseBoolean(params[0]));
        jwt.setExpire(Boolean.parseBoolean(params[1]));
        entityManager.merge(jwt);
    }

    @Transactional
    @Override
    public void delete(Jwt jwt) {
        entityManager.remove(jwt);
    }


}
