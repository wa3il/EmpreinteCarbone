package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import fr.univlyon1.m1if.m1if10.appec.model.Posseder;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JpaPossederDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private JpaPossederDao possederDao;

    private Posseder posseder;
    private User user;
    private Aliment aliment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setName("test");
        user.setUsername("testLogin");
        user.setPassword("testPassword");

        aliment = new Aliment();

        posseder = new Posseder(user, aliment, 2.0f, new Date(new java.util.Date().getTime()));
        // Save the posseder
        possederDao.save(posseder);
        when(entityManager.find(Posseder.class, 1)).thenReturn(posseder);
    }

    @AfterEach
    void tearDown() {
        possederDao.delete(posseder);
    }

    @Test
    void getReturnsPossederWhenPossederExists() {
        Optional<Posseder> result = possederDao.get(1);

        assertTrue(result.isPresent());
        assertSame(posseder, result.get());
    }

    @Test
    void getReturnsEmptyOptionalWhenPossederDoesNotExist() {
        when(entityManager.find(Posseder.class, 1)).thenReturn(null);

        Optional<Posseder> result = possederDao.get(1);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllPossedersTest() {
        // Create your test data
        Posseder posseder1 = new Posseder(user, aliment, 2.0f, new Date(new java.util.Date().getTime()));
        Posseder posseder2 = new Posseder(user, aliment, 2.0f, new Date(new java.util.Date().getTime()));
        List<Posseder> expectedPosseders = Arrays.asList(posseder1, posseder2);

        TypedQuery<Posseder> typedQuery = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT p FROM Posseder p", Posseder.class)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(expectedPosseders);

        List<Posseder> result = possederDao.getAll();

        assertEquals(expectedPosseders, result);
    }

    @Test
    void savePossederTest() {
        Posseder possederTest = new Posseder();
        possederTest.setUser(user);
        possederTest.setQuantity(2.0f);
        possederTest.setDate(new Date(new java.util.Date().getTime()));

        possederDao.save(possederTest);
        verify(entityManager, times(1)).persist(possederTest);

        possederDao.delete(possederTest);
    }

    @Test
    void updatePossederTest() {
        String[] params = {"2.0"};
        possederDao.update(posseder, params);
        assertEquals(2.0f, posseder.getQuantity());
        verify(entityManager, times(1)).merge(posseder);
    }

    @Test
    void deletePossederTest() {
        possederDao.delete(posseder);
        verify(entityManager, times(1)).remove(posseder);
    }

/*    @Test
    void findAlimentsByUserReturnsPossedersWhenPossedersExist() {
        Query queryMock = mock(Query.class);

        when(entityManager.createQuery("SELECT p.user.login, p.aliment, p.quantity, p.date FROM Posseder p WHERE p.user.login = :username")).thenReturn(queryMock);

        when(queryMock.getResultList()).thenReturn(Arrays.asList(posseder));

        List<Posseder> result = possederDao.findAlimentsByUser(user);

        assertEquals(1, result.size());
        assertSame(posseder, result.get(0));
    }

    @Test
    void findAlimentsByUserReturnsEmptyListWhenPossedersDoNotExist() {
        // Create a mock for the Query object
        Query queryMock = mock(Query.class);

        // Stub the call to entityManager.createQuery() to return the mock Query
        when(entityManager.createQuery("SELECT p.user.login, p.aliment, p.quantity, p.date FROM Posseder p WHERE p.user.login = :username")).thenReturn(queryMock);

        // Stub the call to getResultList() on the mock Query to return an empty list
        when(queryMock.getResultList()).thenReturn(Arrays.asList());

        // Call the method under test
        List<Posseder> result = possederDao.findAlimentsByUser(user);

        // Verify the result
        assertTrue(result.isEmpty());
    }

    @Test
    void findAlimentsByUserAndDateReturnsPossedersWhenPossedersExist() {
        Date date = Date.valueOf("2024-01-01");
        when(entityManager.createQuery("SELECT p.user.login, p.aliment, p.quantity, p.date FROM Posseder p WHERE p.user.login = :username AND p.date >= :date")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(posseder));

        List<Posseder> result = possederDao.findAlimentsByUserAndDate(user, date);

        assertEquals(1, result.size());
        assertSame(posseder, result.get(0));
    }

    @Test
    void findAlimentsByUserAndDateReturnsEmptyListWhenPossedersDoNotExist() {
        Date date = new Date(new java.util.Date().getTime());
        when(entityManager.createQuery("SELECT p.user.login, p.aliment, p.quantity, p.date FROM Posseder p WHERE p.user.login = :username AND p.date >= :date")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList());

        List<Posseder> result = possederDao.findAlimentsByUserAndDate(user, date);

        assertTrue(result.isEmpty());
    }
    */

}