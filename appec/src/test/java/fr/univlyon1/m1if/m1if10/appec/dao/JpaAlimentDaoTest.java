/*package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaAlimentDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaAlimentDao jpaAlimentDao;

    private Aliment aliment;

    @BeforeEach
    void setUp() {
        aliment = new Aliment(); // Initialize aliment with your own values
        entityManager.persist(aliment);
        entityManager.flush();
    }

    @Test
    void whenGetById_thenReturnAliment() {
        Optional<Aliment> found = jpaAlimentDao.get(aliment.getAlimentId());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(aliment);
    }

    @Test
    void whenGetAll_thenReturnAllAliments() {
        List<Aliment> aliments = jpaAlimentDao.getAll();

        assertThat(aliments).isNotEmpty();
        assertThat(aliments).contains(aliment);
    }

    @Test
    void whenSave_thenAlimentIsPersisted() {
        Aliment newAliment = new Aliment(); // Initialize newAliment with your own values
        jpaAlimentDao.save(newAliment);

        Optional<Aliment> found = jpaAlimentDao.get(newAliment.getAlimentId());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(newAliment);
    }

    @Test
    void whenUpdate_thenAlimentIsUpdated() {
        String[] params = {}; // Initialize params with your own values
        jpaAlimentDao.update(aliment, params);

        Optional<Aliment> found = jpaAlimentDao.get(aliment.getAlimentId());
        assertThat(found.isPresent()).isTrue();
        // Add assertions to check if the aliment has been updated with the new params
    }

    @Test
    void whenDelete_thenAlimentIsDeleted() {
        jpaAlimentDao.delete(aliment);

        Optional<Aliment> found = jpaAlimentDao.get(aliment.getAlimentId());
        assertThat(found.isPresent()).isFalse();
    }
}*/