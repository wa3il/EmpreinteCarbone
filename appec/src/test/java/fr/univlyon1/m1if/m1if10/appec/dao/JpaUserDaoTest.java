package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class JpaUserDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private JpaUserDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReturnsUserWhenUserExists() {
        User user = new User();
        when(entityManager.find(User.class, 1)).thenReturn(user);

        Optional<User> result = userDao.get(1);

        assertTrue(result.isPresent());
        assertSame(user, result.get());
    }

    @Test
    void getReturnsEmptyOptionalWhenUserDoesNotExist() {
        when(entityManager.find(User.class, 1)).thenReturn(null);

        Optional<User> result = userDao.get(1);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllReturnsAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(entityManager.createQuery("SELECT e FROM User e")).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedUsers);

        List<User> result = userDao.getAll();

        assertEquals(expectedUsers, result);
    }

    @Test
    void savePersistsUser() {
        User user = new User();
        userDao.save(user);
        verify(entityManager, times(1)).persist(user);
    }

    @Test
    void updateUpdatesUser() {
        User user = new User();
        String[] params = {"newName", "newPassword"};
        userDao.update(user, params);
        assertEquals("newName", user.getName());
        assertEquals("newPassword", user.getPassword());
        verify(entityManager, times(1)).merge(user);
    }

    @Test
    void deleteRemovesUser() {
        User user = new User();
        userDao.delete(user);
        verify(entityManager, times(1)).remove(user);
    }

    @Test
    void findByLoginReturnsUserWhenUserExists() {
        User user = new User();
        when(entityManager.createQuery("SELECT e FROM User e WHERE e.login = :login")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(user));

        Optional<User> result = userDao.findByLogin("login");

        assertTrue(result.isPresent());
        assertSame(user, result.get());
    }

    @Test
    void findByLoginReturnsEmptyOptionalWhenUserDoesNotExist() {
        when(entityManager.createQuery("SELECT e FROM User e WHERE e.login = :login")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList());

        Optional<User> result = userDao.findByLogin("login");

        assertFalse(result.isPresent());
    }
}