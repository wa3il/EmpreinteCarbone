package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.AfterEach;
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
class JpaJwtDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private JpaJwtDao jwtDao;

    private Jwt jwt;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setName("test");
        user.setUsername("testLogin");
        user.setPassword("testPassword");

        jwt = new Jwt();
        jwt.setUser(user);
        jwt.setToken("token");
        jwt.setExpire(false);
        jwt.setDesactive(false);

        // Save the jwt
        when(entityManager.find(Jwt.class, 1)).thenReturn(jwt);
    }

    @AfterEach
    void tearDown() {
        // Delete the jwt
        jwtDao.delete(jwt);
    }

    @Test
    void getReturnsJwtWhenJwtExists() {
        Optional<Jwt> result = jwtDao.get(1);

        assertTrue(result.isPresent());
        assertSame(jwt, result.get());
    }

    @Test
    void getReturnsEmptyOptionalWhenJwtDoesNotExist() {
        when(entityManager.find(Jwt.class, 1)).thenReturn(null);

        Optional<Jwt> result = jwtDao.get(1);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllReturnsAllJwts() {
        Jwt jwt1 = new Jwt();
        Jwt jwt2 = new Jwt();
        List<Jwt> expectedJwts = Arrays.asList(jwt1, jwt2);
        when(entityManager.createQuery("SELECT e FROM Jwt e")).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedJwts);

        List<Jwt> result = jwtDao.getAll();

        assertEquals(expectedJwts, result);
    }

    @Test
    void savePersistsJwt() {
        jwtDao.save(jwt);
        verify(entityManager, times(1)).persist(jwt);
    }

    @Test
    void updateUpdatesJwt() {
        String[] params = {"true", "true"};
        jwtDao.update(jwt, params);
        assertTrue(jwt.isDesactive());
        assertTrue(jwt.isExpire());
        verify(entityManager, times(1)).merge(jwt);
    }

    @Test
    void deleteRemovesJwt() {
        jwtDao.delete(jwt);
        verify(entityManager, times(1)).remove(jwt);
    }

    @Test
    void findByValueReturnsJwtWhenJwtExists() {
        when(entityManager.createQuery("SELECT e FROM Jwt e WHERE e.token = :token")).thenReturn(query);
        when(query.getSingleResult()).thenReturn(jwt);

        Optional<Jwt> result = jwtDao.findByValue("token");

        assertTrue(result.isPresent());
        assertSame(jwt, result.get());
    }

    @Test
    void findByValueReturnsEmptyOptionalWhenJwtDoesNotExist() {
        when(entityManager.createQuery("SELECT e FROM Jwt e WHERE e.token = :token")).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        Optional<Jwt> result = jwtDao.findByValue("token");

        assertFalse(result.isPresent());
    }

    @Test
    void findTokenValidByUserReturnsJwtWhenJwtExists() {
        when(entityManager.createQuery("SELECT e FROM Jwt e WHERE e.user = :user AND e.expire = false AND e.desactive = false")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(jwt));

        Optional<Jwt> result = jwtDao.findTokenValidByUser(user);

        assertTrue(result.isPresent());
        assertSame(jwt, result.get());
    }

    @Test
    void findTokenValidByUserReturnsEmptyOptionalWhenJwtDoesNotExist() {
        when(entityManager.createQuery("SELECT e FROM Jwt e WHERE e.user = :user AND e.expire = false AND e.desactive = false")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList());

        Optional<Jwt> result = jwtDao.findTokenValidByUser(user);

        assertFalse(result.isPresent());
    }
}