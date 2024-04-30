package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaAlimentDao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaPossederDao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaUserDao;
import fr.univlyon1.m1if.m1if10.appec.model.Posseder;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserRessourceControllerTest {

    @InjectMocks
    private UserRessourceController userRessourceController;

    @Mock
    private JpaUserDao jpaUserDao;

    @Mock
    private JpaAlimentDao jpaAlimentDao;

    @Mock
    private JpaPossederDao jpaPossederDao;

    @Mock
    private AuthenticationService authService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a user
        user = new User();
        user.setUsername("login");
        user.setPassword("password");
        user.setName("name");

        // Save the user
        when(jpaUserDao.findByLogin(anyString())).thenReturn(Optional.of(user));
    }

    @AfterEach
    void tearDown() {
        // Delete the user
        jpaUserDao.delete(user);

    }

    @Test
    void getAllUser() {
        List<User> users = new ArrayList<>();
        when(jpaUserDao.getAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userRessourceController.getAllUser();

        assertEquals(200, response.getStatusCodeValue());
        verify(jpaUserDao, times(1)).getAll();
    }

    @Test
    void getUser() {
        ResponseEntity<Object> response = userRessourceController.getUser(user.getUsername());

        assertEquals(200, response.getStatusCodeValue());
        verify(jpaUserDao, times(1)).findByLogin(anyString());
    }
    
    @Test
    void deleteUser() {
        ResponseEntity<String> response = userRessourceController.deleteUser(user.getUsername());

        assertEquals(200, response.getStatusCodeValue());
        verify(jpaUserDao, times(1)).delete(any(User.class));
    }

    @Test
    void getAlimentsUserAndDate() {
        List<Posseder> posseders = new ArrayList<>();
        when(jpaPossederDao.findAlimentsByUserAndDate(any(User.class), any(Date.class))).thenReturn(posseders);

        ResponseEntity<List<Posseder>> response = userRessourceController.getAlimentsUser(user.getUsername(), "2022-01-01");

        assertEquals(200, response.getStatusCodeValue());
        verify(jpaPossederDao, times(1)).findAlimentsByUserAndDate(any(User.class), any(Date.class));
    }


}