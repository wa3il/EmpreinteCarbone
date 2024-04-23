package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of the DAO for Aliment.
 */
@Repository
public class JpaAlimentDao implements Dao<Aliment> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Aliment> get(Integer id) {
        return Optional.ofNullable(entityManager.find(Aliment.class, id));
    }

    @Override
    public List<Aliment> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Aliment e");
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(Aliment aliment) {
        // entityManager.persist(aliment);
    }

    @Transactional
    @Override
    public void update(Aliment aliment, String[] params) {
        // Objects.requireNonNull(params, "Params cannot be null");
        // Objects.requireNonNull(params[0], "NomLegume cannot be null");
        // Objects.requireNonNull(params[1], "EC cannot be null");
        // Objects.requireNonNull(params[2], "Groupe cannot be null");
        // Objects.requireNonNull(params[3], "SousGroupe cannot be null");

        // aliment.setNomLegume(params[0]);
        // aliment.setEc(Float.parseFloat(params[1]));
        // aliment.setGroupe(params[2]);
        // aliment.setSousGroupe(params[3]);
        // entityManager.merge(aliment);
    }

    @Transactional
    @Override
    public void delete(Aliment aliment) {
        //entityManager.remove(aliment);
    }
}
