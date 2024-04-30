package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserBusinessControllerTest {

    @InjectMocks
    private UserBusinessController userBusinessController;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createUser() throws JsonProcessingException {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setPassword("password");
        userRequestDto.setName("name");
        when(authenticationService.register(any(UserRequestDto.class))).thenReturn(new AuthenticationResponse("token"));

        ResponseEntity<AuthenticationResponse> response = userBusinessController.createUser("{\"login\" : \"login\", \"name\" : \"name\", \"password\" : \"password\"}", MediaType.APPLICATION_JSON_VALUE);

        assertEquals(200, response.getStatusCodeValue());
        verify(authenticationService, times(1)).register(any(UserRequestDto.class));
    }

    @Test
    void login() throws JsonProcessingException {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setLogin("login");
        userRequestDto.setPassword("password");
        when(authenticationService.authenticate(any(UserRequestDto.class))).thenReturn(new AuthenticationResponse("token"));

        ResponseEntity<AuthenticationResponse> response = userBusinessController.login("{\"login\" : \"login\", \"password\" : \"password\"}", MediaType.APPLICATION_JSON_VALUE);

        assertEquals(200, response.getStatusCodeValue());
        verify(authenticationService, times(1)).authenticate(any(UserRequestDto.class));
    }

    @Test
    void logout() {
        doNothing().when(authenticationService).deconnexion();

        ResponseEntity<String> response = userBusinessController.logout();

        assertEquals(200, response.getStatusCodeValue());
        verify(authenticationService, times(1)).deconnexion();
    }

    @Test
    void logoutReturnsUnauthorizedWhenExceptionIsThrown() {
        doThrow(new RuntimeException()).when(authenticationService).deconnexion();

        ResponseEntity<String> response = userBusinessController.logout();

        assertEquals(401, response.getStatusCodeValue());
    }
}