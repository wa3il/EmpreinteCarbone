package fr.univlyon1.m1if.m1if10.appec.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private final User user = new User("toto", "password", "Xyz");

    @Test
    void getName() {
        assertEquals("toto",user.getName());
    }

    @Test
    void setName() {
        user.setName("Abc");
        assertEquals("Abc", user.getName());
    }

    @Test
    void getPassword() {
        assertEquals( "password", user.getPassword());
    }

    @Test
    void setPassword() {
        user.setPassword("password1");
        assertEquals("password1",user.getPassword());
    }

    @Test
    void getUserMain() {
        assertEquals("Xyz", user.getUsername());
    }

}