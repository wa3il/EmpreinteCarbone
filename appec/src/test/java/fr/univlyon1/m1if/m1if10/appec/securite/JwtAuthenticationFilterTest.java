package fr.univlyon1.m1if.m1if10.appec.securite;

import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");

        jwt = new Jwt();
        jwt.setUser(user);
        jwt.setToken("testToken");
    }


    @Test
    void doFilterInternalTest() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt.getToken());
        when(jwtService.extractUserLogin(jwt.getToken())).thenReturn(user.getUsername());
        when(jwtService.tokenByValue(jwt.getToken())).thenReturn(jwt);

        UserDetails userDetails = user;
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt.getToken(), userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }


    @Test
    void doFilterInternalTestWithNoToken() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}