package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JpaUserDao implements Dao<User> {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Optional<User> get(Integer id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM User e");
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Transactional
    @Override
    public void update(User user, String[] params) {
        Objects.requireNonNull(params, "Params cannot be null");
        Objects.requireNonNull(params[0], "Name cannot be null");
        Objects.requireNonNull(params[1], "Password cannot be null");
        Objects.requireNonNull(params[2], "Email cannot be null");

        user.setName(params[0]);
        user.setPassword(params[1]);
        user.setEmail(params[2]);
        entityManager.merge(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

}
