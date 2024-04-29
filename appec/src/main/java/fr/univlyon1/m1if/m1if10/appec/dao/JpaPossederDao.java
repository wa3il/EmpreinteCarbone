package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Posseder;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA implementation of the DAO for Posseder.
 */
@Repository
public class JpaPossederDao implements Dao<Posseder>{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Optional<Posseder> get(Integer id) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public List<Posseder> getAll() {
        return entityManager.createQuery("SELECT p FROM Posseder p", Posseder.class).getResultList();
    }

    @Transactional
    @Override
    public void save(Posseder posseder) {
        entityManager.persist(posseder);
    }

    @Transactional
    @Override
    public void update(Posseder posseder, String[] params) {
        Objects.requireNonNull(params, "Params cannot be null");
        Objects.requireNonNull(params[0], "Quantity cannot be null");
        posseder.setQuantity(Float.parseFloat(params[0]));
        entityManager.merge(posseder);
    }

    @Override
    public void delete(Posseder posseder) {
        entityManager.remove(posseder);
    }

    @Transactional
    public List<Posseder> findAlimentsByUser(User user) {
        Query query = entityManager.createQuery(
                "SELECT p.user.login, p.aliment, p.quantity, p.date   FROM Posseder p " +
                        "WHERE p.user.login = :username"
        );

        query.setParameter("username", user.getUsername());
        List<Posseder> posseders = query.getResultList();
        return posseders;
    }

    @Transactional
    public List<Posseder> findAlimentsByUserAndDate(User user, Date date) {
        Query query = entityManager.createQuery(
                "SELECT p.user.login, p.aliment, p.quantity, p.date   FROM Posseder p " +
                        "WHERE p.user.login = :username AND p.date >= :date"
        );

        query.setParameter("username", user.getUsername());
        query.setParameter("date", date);
        List<Posseder> posseders = query.getResultList();
        return posseders;
    }

}
