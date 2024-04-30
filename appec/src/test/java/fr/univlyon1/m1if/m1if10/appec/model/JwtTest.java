package fr.univlyon1.m1if.m1if10.appec.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTest {

    @Test
    void testGetAndSetJwtId() {

        Jwt jwt = new Jwt();

        jwt.setJwtId(123);

        assertEquals(123, jwt.getJwtId());
    }

    @Test
    void testIsAndSetDesactive() {

        Jwt jwt = new Jwt();

        jwt.setDesactive(true);

        assertTrue(jwt.isDesactive());
    }

    @Test
    void testIsAndSetExpire() {

        Jwt jwt = new Jwt();

        jwt.setExpire(true);

        assertTrue(jwt.isExpire());
    }

    @Test
    void testGetAndSetUser() {

        Jwt jwt = new Jwt();

        User user = new User("John", "password", "john123");

        jwt.setUser(user);

        assertEquals(user, jwt.getUser());
    }

    @Test
    void testGetAndSetToken() {

        Jwt jwt = new Jwt();

        jwt.setToken("abc123");

        assertEquals("abc123", jwt.getToken());
    }

    @Test
    void testConstructors() {
        Jwt jwt1 = new Jwt();
        assertFalse(jwt1.isDesactive());
        assertFalse(jwt1.isExpire());

        User user = new User();
        Jwt jwt2 = new Jwt(true, true, user);
        assertTrue(jwt2.isDesactive());
        assertTrue(jwt2.isExpire());
        assertEquals(user, jwt2.getUser());
    }
}
