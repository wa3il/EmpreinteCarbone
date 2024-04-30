package fr.univlyon1.m1if.m1if10.appec.dto;

import org.junit.jupiter.api.Test;

import fr.univlyon1.m1if.m1if10.appec.dto.user.AddEcRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

public class AddEcRequestDtoTest {

    @Test
    public void testConstructorAndGetters() {
        // Create a Date object
        Date date = new Date(System.currentTimeMillis());

        // Create AddEcRequestDto object using constructor
        AddEcRequestDto dto = new AddEcRequestDto(1, 10.5f, "testUser", date);

        // Test getter methods
        assertEquals(1, dto.getAlimentId());
        assertEquals(10.5f, dto.getQuantity(), 0.001);
        assertEquals("testUser", dto.getLogin());
        assertEquals(date, dto.getDate());
    }

    @Test
    public void testSetters() {
        // Create a Date object
        Date date = new Date(System.currentTimeMillis());

        // Create AddEcRequestDto object
        AddEcRequestDto dto = new AddEcRequestDto();

        // Test setter methods
        dto.setAlimentId(2);
        dto.setQuantity(20.0f);
        dto.setLogin("anotherUser");
        dto.setDate(date);

        // Test getter methods
        assertEquals(2, dto.getAlimentId());
        assertEquals(20.0f, dto.getQuantity(), 0.001);
        assertEquals("anotherUser", dto.getLogin());
        assertEquals(date, dto.getDate());
    }
}
