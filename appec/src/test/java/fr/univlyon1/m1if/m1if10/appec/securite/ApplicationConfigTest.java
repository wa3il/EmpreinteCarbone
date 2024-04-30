package fr.univlyon1.m1if.m1if10.appec.securite;

import fr.univlyon1.m1if.m1if10.appec.dao.JpaUserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationConfigTest {

    @Mock
    private JpaUserDao jpaUserDao;

    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationConfig = new ApplicationConfig(jpaUserDao);
    }

    @Test
    void userDetailsService() {
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertNotNull(userDetailsService);
    }

    @Test
    void authenticationProvider() {
        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();
        assertNotNull(authenticationProvider);
        assertTrue(authenticationProvider instanceof DaoAuthenticationProvider);
    }

    @Test
    void passwordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
}