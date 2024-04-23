package fr.univlyon1.m1if.m1if10.appec.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private final User user = new User("toto", "password", "Xyz");

    @Test
    void getName() {
        assertEquals(user.getName(), "toto");
    }

    @Test
    void setName() {
        user.setName("Abc");
        assertEquals(user.getName(), "Abc");
    }

    @Test
    void getPassword() {
        assertEquals(user.getPassword(), "password");
    }

    @Test
    void setPassword() {
        user.setPassword("password1");
        assertEquals(user.getPassword(), "password1");
    }

    @Test
    void getUserMain() {
        assertEquals(user.getUsername(), "Xyz");
    }

}