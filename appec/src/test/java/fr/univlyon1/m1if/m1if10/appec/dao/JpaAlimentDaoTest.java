package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JpaAlimentDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private JpaAlimentDao alimentDao;

    private Aliment aliment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        aliment = new Aliment();
        aliment.setNomLegume("testAliment");

        // Save the aliment
        when(entityManager.find(Aliment.class, 1)).thenReturn(aliment);
    }

    @Test
    void getReturnsAlimentWhenAlimentExists() {
        Optional<Aliment> result = alimentDao.get(1);

        assertTrue(result.isPresent());
        assertSame(aliment, result.get());
    }

    @Test
    void getReturnsEmptyOptionalWhenAlimentDoesNotExist() {
        when(entityManager.find(Aliment.class, 1)).thenReturn(null);

        Optional<Aliment> result = alimentDao.get(1);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllReturnsAllAliments() {
        Aliment aliment1 = new Aliment();
        Aliment aliment2 = new Aliment();
        List<Aliment> expectedAliments = Arrays.asList(aliment1, aliment2);
        when(entityManager.createQuery("SELECT e FROM Aliment e")).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedAliments);

        List<Aliment> result = alimentDao.getAll();

        assertEquals(expectedAliments, result);
    }

    @Test
    void update() {

        String[] params = {"newName"};
        alimentDao.update(aliment, params);
        assertEquals("newName", aliment.getNomLegume());
        verify(entityManager, times(1)).merge(aliment);
    }
}