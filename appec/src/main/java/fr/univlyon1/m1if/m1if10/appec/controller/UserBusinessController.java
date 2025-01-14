package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import static fr.univlyon1.m1if.m1if10.appec.controller.Mapdata.*;

/**
 * Controller for user business.
 * This controller is used to manage user connection and disconnection.
 */
@RequestMapping("/users")
@RestController
public class UserBusinessController {

    private final AuthenticationService authenticationService;

    @Autowired
    public UserBusinessController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Create a user.
     *
     * @param requestBody the request body
     * @param contentType the content type
     * @return the response entity
     */
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody String requestBody, @RequestHeader("Content-Type") String contentType) throws JsonProcessingException {
        Optional<UserRequestDto> userRequestDto = getUserDtoRequest(requestBody, contentType);
        if (userRequestDto.isPresent() && userRequestDto.get().getPassword() != null && userRequestDto.get().getName() != null){
            return ResponseEntity.ok(authenticationService.register(userRequestDto.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/login",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<AuthenticationResponse> login(@RequestBody String requestBody, @RequestHeader("Content-Type") String contentType) throws JsonProcessingException {
        Optional<UserRequestDto> userRequest = getUserDtoRequest(requestBody, contentType);
        if (userRequest.isPresent() && userRequest.get().getPassword() != null){
            return ResponseEntity.ok(authenticationService.authenticate(userRequest.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout() {
        try {
            authenticationService.deconnexion();
            return ResponseEntity.ok("Déconnexion réussie");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
