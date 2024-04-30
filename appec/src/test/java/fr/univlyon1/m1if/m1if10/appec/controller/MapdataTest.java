package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AddEcRequestDto;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MapdataTest {

    @Test
    void testExtractFormData() {
        String requestBody = "key1=value1&key2=value2";
        Map<String, String> formData = Mapdata.extractFormData(requestBody);

        assertEquals("value1", formData.get("key1"));
        assertEquals("value2", formData.get("key2"));
    }

    @Test
    void testGetUserDtoRequestJson() throws JsonProcessingException {
        String requestBody = "{\"login\":\"testLogin\",\"password\":\"testPassword\",\"name\":\"testName\"}";
        String contentType = "application/json";

        Optional<UserRequestDto> userRequestDto = Mapdata.getUserDtoRequest(requestBody, contentType);

        assertTrue(userRequestDto.isPresent());
        assertEquals("testLogin", userRequestDto.get().getLogin());
        assertEquals("testPassword", userRequestDto.get().getPassword());
        assertEquals("testName", userRequestDto.get().getName());
    }

    @Test
    void testGetUserDtoRequestForm() throws JsonProcessingException {
        String requestBody = "login=testLogin&password=testPassword&name=testName";
        String contentType = "application/x-www-form-urlencoded";

        Optional<UserRequestDto> userRequestDto = Mapdata.getUserDtoRequest(requestBody, contentType);

        assertTrue(userRequestDto.isPresent());
        assertEquals("testLogin", userRequestDto.get().getLogin());
        assertEquals("testPassword", userRequestDto.get().getPassword());
        assertEquals("testName", userRequestDto.get().getName());
    }

    @Test
    void testGetAddEcDtoRequestJson() throws JsonProcessingException {
        String requestBody = "{\"login\":\"testLogin\",\"alimentId\":\"1\",\"quantity\":\"2.0\",\"date\":\"2022-01-01\"}";
        String contentType = "application/json";

        Optional<AddEcRequestDto> addEcRequestDto = Mapdata.getAddEcDtoRequest(requestBody, contentType);

        assertTrue(addEcRequestDto.isPresent());
        assertEquals("testLogin", addEcRequestDto.get().getLogin());
        assertEquals(1, addEcRequestDto.get().getAlimentId());
        assertEquals(2.0, addEcRequestDto.get().getQuantity());
        assertEquals(java.sql.Date.valueOf("2022-01-01").toString(), addEcRequestDto.get().getDate().toString());
    }

    @Test
    void testGetAddEcDtoRequestForm() throws JsonProcessingException {
        String requestBody = "login=testLogin&alimentId=1&quantite=2.0&date=2022-01-01";
        String contentType = "application/x-www-form-urlencoded";

        Optional<AddEcRequestDto> addEcRequestDto = Mapdata.getAddEcDtoRequest(requestBody, contentType);

        assertTrue(addEcRequestDto.isPresent());
        assertEquals("testLogin", addEcRequestDto.get().getLogin());
        assertEquals(1, addEcRequestDto.get().getAlimentId());
        assertEquals(2.0, addEcRequestDto.get().getQuantity());
        assertEquals(java.sql.Date.valueOf("2022-01-01"), addEcRequestDto.get().getDate());
    }

    @Test
    void testGetUserDtoRequestJsonInvalid() throws JsonProcessingException {
        String requestBody = "{\"login\":\"\",\"password\":\"\"}";
        String contentType = "application/json";

        Optional<UserRequestDto> userRequestDto = Mapdata.getUserDtoRequest(requestBody, contentType);

        assertFalse(userRequestDto.isPresent());
    }

    @Test
    void testGetUserDtoRequestFormInvalid() throws JsonProcessingException {
        String requestBody = "login=&password=";
        String contentType = "application/x-www-form-urlencoded";

        Optional<UserRequestDto> userRequestDto = Mapdata.getUserDtoRequest(requestBody, contentType);

        assertFalse(userRequestDto.isPresent());
    }

    @Test
    void testGetAddEcDtoRequestJsonInvalid() throws JsonProcessingException {
        String requestBody = "{\"login\":\"\",\"alimentId\":\"0\",\"quantity\":\"0.0\"}";
        String contentType = "application/json";

        Optional<AddEcRequestDto> addEcRequestDto = Mapdata.getAddEcDtoRequest(requestBody, contentType);

        assertFalse(addEcRequestDto.isPresent());
    }


}