package fr.univlyon1.m1if.m1if10.appec.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

public class PossederTest {

    private Posseder posseder;
    private User user;
    private Aliment aliment;
    private Date date;

    @BeforeEach
    public void setUp() {
        user = new User("John", "password", "john@example.com"); // Create a User object for testing
        aliment = new Aliment("Carrot", 0.5f, "Vegetable", "Root"); // Create an Aliment object for testing
        date = new Date(System.currentTimeMillis()); // Current date for testing
        posseder = new Posseder(user, aliment, 10.0f, date); // Initialize Posseder for testing
    }

    @Test
    public void testGetters() {
        assertEquals(user, posseder.getUser());
        assertEquals(aliment, posseder.getAliment());
        assertEquals(10.0f, posseder.getQuantity(), 0.001); // Check with delta for float comparison
        assertEquals(date, posseder.getDate());
    }

    @Test
    public void testSetters() {
        User newUser = new User("Jane", "password", "jane@example.com");
        Aliment newAliment = new Aliment("Apple", 0.3f, "Fruit", "Pome");
        Date newDate = new Date(0); // Some arbitrary date for testing

        posseder.setUser(newUser);
        posseder.setAliment(newAliment);
        posseder.setQuantity(20.0f);
        posseder.setDate(newDate);

        assertEquals(newUser, posseder.getUser());
        assertEquals(newAliment, posseder.getAliment());
        assertEquals(20.0f, posseder.getQuantity(), 0.001); // Check with delta for float comparison
        assertEquals(newDate, posseder.getDate());
    }

    @Test
    public void testConstructor() {
        assertNotNull(posseder);
        assertEquals(user, posseder.getUser());
        assertEquals(aliment, posseder.getAliment());
        assertEquals(10.0f, posseder.getQuantity(), 0.001); // Check with delta for float comparison
        assertEquals(date, posseder.getDate());
    }
}
