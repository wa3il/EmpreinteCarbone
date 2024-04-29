package fr.univlyon1.m1if.m1if10.appec.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
     void testGetUsername() {
        User user = new User("John", "password123", "john123");
        assertEquals("john123", user.getUsername());
    }

    @Test
     void testGetAuthorities() {
        User user = new User("John", "password123", "john123");
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
     void testIsAccountNonExpired() {
        User user = new User("John", "password123", "john123");
        assertTrue(user.isAccountNonExpired());
    }

    @Test
     void testIsAccountNonLocked() {
        User user = new User("John", "password123", "john123");
        assertTrue(user.isAccountNonLocked());
    }

    @Test
     void testIsCredentialsNonExpired() {
        User user = new User("John", "password123", "john123");
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
     void testIsEnabled() {
        User user = new User("John", "password123", "john123");
        assertTrue(user.isEnabled());
    }
}
