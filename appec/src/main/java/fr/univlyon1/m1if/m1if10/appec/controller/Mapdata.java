package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AddEcRequestDto;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Extract form data from a request body.
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@Component
public class Mapdata {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Private constructor to hide the implicit public one
    private Mapdata() {
        // Do nothing, just to hide the constructor
    }

    public static Map<String, String> extractFormData(String requestBody) {
        Map<String, String> formData = new HashMap<>();
        String[] pairs = requestBody.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (Exception e) {
                    // Ignore or handle the exception
                }
                formData.put(key, value);
            }
        }
        return formData;
    }


    /**
     * Get user request dto.
     *
     * @param requestBody the request body
     * @param contentType the content type
     * @return the user request dto
     * @throws JsonProcessingException the json processing exception
     */
    public static Optional<UserRequestDto> getUserDtoRequest(String requestBody, String contentType) throws JsonProcessingException {
        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            UserRequestDto userRequest = objectMapper.readValue(requestBody, UserRequestDto.class);
            if (userRequest.getLogin().isBlank() || userRequest.getLogin() == null || userRequest.getPassword().isBlank() || userRequest.getPassword() == null) {
                return Optional.empty();
            } else {
                return Optional.of(userRequest);
            }
        }
        if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            Map<String, String> formData = extractFormData(requestBody);
            String login = formData.get("login");
            String name = formData.get("name");
            String password = formData.get("password");

            UserRequestDto userRequest = new UserRequestDto();
            userRequest.setLogin(login);
            userRequest.setPassword(password);
            userRequest.setName(name);
            if (login == null || password == null) {
                return Optional.empty();
            }
            if(login.isBlank() || password.isBlank()){
                return Optional.empty();
            }
            return Optional.of(userRequest);
        }
        return Optional.empty();

    }

    public static Optional<AddEcRequestDto> getAddEcDtoRequest(String requestBody, String contentType) throws JsonProcessingException {
        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            AddEcRequestDto addEcRequest = objectMapper.readValue(requestBody, AddEcRequestDto.class);
            if (addEcRequest.getQuantity() == 0 || addEcRequest.getLogin() == null ) {
                return Optional.empty();
            } else {
                return Optional.of(addEcRequest);
            }
        }
        if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            Map<String, String> formData = extractFormData(requestBody);
            String login = formData.get("userLogin");
            String alimentId = formData.get("alimentId");
            String quantite = formData.get("quantite");
            String date = formData.get("date");


            AddEcRequestDto addEcRequest = new AddEcRequestDto();
            addEcRequest.setAlimentId(Integer.parseInt(alimentId));
            addEcRequest.setLogin(login);
            addEcRequest.setQuantity(Float.parseFloat(quantite));
            if (!date.isBlank()) {
                addEcRequest.setDate(java.sql.Date.valueOf(date));
            }
            if (Float.parseFloat(quantite) == 0 || login == null){
                return Optional.empty();
            }
            return Optional.of(addEcRequest);
        }
        return Optional.empty();

    }

}
