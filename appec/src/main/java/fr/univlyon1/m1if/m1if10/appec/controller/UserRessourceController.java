package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if10.appec.dao.UserDao;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;
import java.util.Optional;

import static fr.univlyon1.m1if.m1if10.appec.controller.Mapdata.*;

/**
 * Controller for user resource.
 * This controller is used to manage user resources.
 */
@RestController
@RequestMapping("/users")
public class UserRessourceController {

    @Autowired
    private UserDao UserDao;


    private final AuthenticationService authService;

    @Autowired
    public UserRessourceController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Get all users.
     *
     * @return a list of users
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(UserDao.getAll());
    }

    /**
     * Get a user by id.
     *
     * @param id the user id
     * @return the user
     */
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getUser(@PathVariable("id") final Integer id) {
        Optional<User> user = UserDao.get(id);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
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
        if (userRequestDto.isPresent()) {
            return ResponseEntity.ok(authService.register(userRequestDto.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update a user.
     *
     * @param id the user id
     * @param requestBody the request body
     * @param contentType the content type
     * @return the response entity
     */
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> updateUser(
            @PathVariable("id") final Integer id,
            @RequestBody String requestBody,
            @RequestHeader("Content-Type") String contentType) {
        try {
            Optional<UserRequestDto> requestDto = getUserDtoRequest(requestBody, contentType);
            if (requestDto.isPresent()) {
                Optional<User> user = UserDao.get(id);
                if (user.isPresent()) {
                    UserRequestDto userdto = requestDto.get();
                    UserDao.update(user.get(),new String[]{userdto.getName(), userdto.getPassword(), userdto.getLogin()});
                    return ResponseEntity.ok("Utilisateur mis à jour");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Type de média non pris en charge.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la mise à jour de l'utilisateur.");
        }
    }

    /**
     * Delete a user.
     *
     * @param id the user id
     * @return the response entity
     */
    @DeleteMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> deleteUser(@PathVariable("id") final Integer id) {
        try {
            Optional<User> user = UserDao.get(id);
            if (user.isPresent()) {
                UserDao.delete(user.get());
                return ResponseEntity.ok("Utilisateur supprimé");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la suppression de l'utilisateur");
        }
    }
}

