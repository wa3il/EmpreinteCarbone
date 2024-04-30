package fr.univlyon1.m1if.m1if10.appec.dto;

import org.junit.jupiter.api.Test;

import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;

import static org.junit.jupiter.api.Assertions.*;

 class UserRequestDtoTest {

    @Test
     void testConstructorAndGetters() {
        String login = "user123";
        String password = "password123";
        String name = "John Doe";

        UserRequestDto user = new UserRequestDto(login, password, name);

        assertEquals(login, user.getLogin());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
    }

    @Test
     void testSetters() {
        UserRequestDto user = new UserRequestDto();

        String login = "newUser123";
        String password = "newPassword123";
        String name = "Jane Doe";

        user.setLogin(login);
        user.setPassword(password);
        user.setName(name);

        assertEquals(login, user.getLogin());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
    }
}
