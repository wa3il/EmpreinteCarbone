package fr.univlyon1.m1if.m1if10.appec.dto;

import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String expectedToken = "testToken";

        // Act
        AuthenticationResponse response = new AuthenticationResponse(expectedToken);

        // Assert
        assertEquals(expectedToken, response.getToken());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        AuthenticationResponse response = new AuthenticationResponse();
        String expectedToken = "newToken";

        // Act
        response.setToken(expectedToken);

        // Assert
        assertEquals(expectedToken, response.getToken());
    }
}
