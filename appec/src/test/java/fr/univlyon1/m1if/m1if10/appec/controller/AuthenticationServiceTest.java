package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.JpaJwtDao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaUserDao;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import fr.univlyon1.m1if.m1if10.appec.securite.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private JpaUserDao jpaUserDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JpaJwtDao jpajwtDao;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserRequestDto userRequestDto;
    private User user;
    private Jwt jwt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userRequestDto = new UserRequestDto();
        userRequestDto.setName("test");
        userRequestDto.setLogin("testLogin");
        userRequestDto.setPassword("testPassword");

        user = new User(userRequestDto.getName(), userRequestDto.getPassword(), userRequestDto.getLogin());

        jwt = new Jwt();
        jwt.setUser(user);
        jwt.setToken("token");
        jwt.setExpire(false);
        jwt.setDesactive(false);

        // Save the user
        when(jpaUserDao.findByLogin(anyString())).thenReturn(Optional.of(user));
    }

    @AfterEach
    public void tearDown() {
        // Delete the user
        jpaUserDao.delete(user);
    }

    @Test
    void testRegister() {
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        AuthenticationResponse response = authenticationService.register(userRequestDto);

        assertEquals("token", response.getToken());
        verify(jpaUserDao, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(userRequestDto.getPassword());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void testAuthenticate() {
        when(jpajwtDao.findTokenValidByUser(user)).thenReturn(Optional.empty());
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse response = authenticationService.authenticate(userRequestDto);

        assertEquals("token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getLogin(), userRequestDto.getPassword()));
    }

    @Test
    void testAuthenticateWithValidToken() {
        when(jpajwtDao.findTokenValidByUser(user)).thenReturn(Optional.of(jwt));
        when(jwtService.isTokenValid(jwt.getToken(), user)).thenReturn(true);

        AuthenticationResponse response = authenticationService.authenticate(userRequestDto);

        assertEquals("token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getLogin(), userRequestDto.getPassword()));
    }

    @Test
    void testDeconnexion() {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn(user);
        when(jpajwtDao.findTokenValidByUser(user)).thenReturn(Optional.of(jwt));

        authenticationService.deconnexion();

        verify(jpajwtDao, times(1)).delete(jwt);
    }

    @Test
    void testEncoderPassword() {
        String password = "testPassword";
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        String encodedPassword = authenticationService.encoderPassword(password);

        assertEquals("encodedPassword", encodedPassword);
        verify(passwordEncoder, times(1)).encode(password);
    }
}