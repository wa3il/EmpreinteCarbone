package fr.univlyon1.m1if.m1if10.appec.securite;

import fr.univlyon1.m1if.m1if10.appec.dao.JpaJwtDao;
import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @Mock
    private JpaJwtDao jpaJwtDao;

    @InjectMocks
    private JwtService jwtService;

    private User user;
    private Jwt jwt;

    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");

        jwt = new Jwt();
        jwt.setUser(user);
        jwt.setToken("testToken");

        // Generate the JWT
        token = jwtService.generateToken(user);

        // Verify that save was called
        verify(jpaJwtDao, times(1)).save(any(Jwt.class));
    }

    @AfterEach
    void tearDown() {
        // Delete the JWT
        jwtService.desactiveToken(user);
    }


    @Test
    void generateTokenTest() {

        assertNotNull(token);
        verify(jpaJwtDao, times(1)).save(any(Jwt.class));
    }

    @Test
    void isTokenValidTest() {
        UserDetails userDetails = user;

        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenInvalidTest() {
        UserDetails userDetails = user;

        String token = "invalidToken";
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void tokenByValueTest() {
        when(jpaJwtDao.findByValue("testToken")).thenReturn(Optional.of(jwt));

        Jwt result = jwtService.tokenByValue("testToken");

        assertNotNull(result);
        assertEquals(jwt, result);
    }

    @Test
    void desactiveTokenTest() {
        when(jpaJwtDao.findTokenValidByUser(user)).thenReturn(Optional.of(jwt));

        jwtService.desactiveToken(user);

        verify(jpaJwtDao, times(1)).delete(jwt);
    }
}