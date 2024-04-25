package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

//    public static Optional<User> getUserRequest(String requestBody, String contentType) throws JsonProcessingException {
//        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
//            User user = objectMapper.readValue(requestBody, User.class);
//            if (user.getUsername() == null || user.getPassword() == null) {
//                return Optional.empty();
//            } else {
//                return Optional.of(user);
//            }
//        } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
//            Map<String, String> formData = extractFormData(requestBody);
//            String login = formData.get("login");
//            String password = formData.get("password");
//            if (login == null || password == null) {
//                return Optional.empty();
//            }
//            User user = new User();
//            user.setUsername(login);
//            user.setPassword(password);
//            return Optional.of(user);
//        }
//        return Optional.empty();
//    }

    public static Optional<UserRequestDto> getUserDtoRequest(String requestBody, String contentType) throws JsonProcessingException {
        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            UserRequestDto userRequest = objectMapper.readValue(requestBody, UserRequestDto.class);
            if (userRequest.getLogin() == null || userRequest.getPassword() == null) {
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
            return Optional.of(userRequest);
        }
        return Optional.empty();

    }

}
