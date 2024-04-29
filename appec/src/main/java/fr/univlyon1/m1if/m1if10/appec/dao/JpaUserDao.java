package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA implementation of the DAO for User.
 */
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
        if (params.length == 0) {
            throw new IllegalArgumentException("Params must contain at least one value");
        }
        if (!params[0].isEmpty() && !params[0].isBlank()){
            user.setName(params[0]);
        }
        if (params.length > 1 && !params[1].isEmpty() && !params[1].isBlank()) {
            user.setPassword(params[1]);
        }
        entityManager.merge(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Transactional
    public Optional<User> findByLogin(String login) {
        Query query = entityManager.createQuery(
                "SELECT e FROM User e WHERE e.login = :login"
        );
        query.setParameter("login", login);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

}
