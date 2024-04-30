package fr.univlyon1.m1if.m1if10.appec.dto;

import org.junit.jupiter.api.Test;

import fr.univlyon1.m1if.m1if10.appec.dto.user.UserResponseDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class UserResponseDtoTest {

    @Test
     void testConstructorAndGetters() {
        String login = "testLogin";
        String name = "testName";
        UserResponseDto userResponseDto = new UserResponseDto(login, name);

        assertEquals(login, userResponseDto.getLogin());
        assertEquals(name, userResponseDto.getName());
    }

    @Test
     void testSetters() {
        UserResponseDto userResponseDto = new UserResponseDto();

        String login = "newLogin";
        String name = "newName";

        userResponseDto.setLogin(login);
        userResponseDto.setName(name);

        assertEquals(login, userResponseDto.getLogin());
        assertEquals(name, userResponseDto.getName());
    }

    @Test
     void testDefaultConstructor() {
        UserResponseDto userResponseDto = new UserResponseDto();

        assertEquals(null, userResponseDto.getLogin());
        assertEquals(null, userResponseDto.getName());
    }
}
