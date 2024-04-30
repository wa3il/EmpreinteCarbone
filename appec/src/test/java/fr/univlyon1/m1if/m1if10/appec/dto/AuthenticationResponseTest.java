package fr.univlyon1.m1if.m1if10.appec.dto;

import org.junit.jupiter.api.Test;

import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationResponseTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String expectedToken = "testToken";
        
        // Act
        AuthenticationResponse response = new AuthenticationResponse(expectedToken);
        
        // Assert
        assertEquals(expectedToken, response.getToken());
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        AuthenticationResponse response = new AuthenticationResponse();
        String expectedToken = "newToken";
        
        // Act
        response.setToken(expectedToken);
        
        // Assert
        assertEquals(expectedToken, response.getToken());
    }
}
