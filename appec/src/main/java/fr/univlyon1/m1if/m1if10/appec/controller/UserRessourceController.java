package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.univlyon1.m1if.m1if10.appec.dao.JpaAlimentDao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaPossederDao;
import fr.univlyon1.m1if.m1if10.appec.dao.UserDao;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AddEcRequestDto;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import fr.univlyon1.m1if.m1if10.appec.model.Posseder;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

import static fr.univlyon1.m1if.m1if10.appec.controller.Mapdata.*;

/**
 * Controller for user resource.
 * This controller is used to manage user resources.
 */
@RestController
@RequestMapping("/users")
public class UserRessourceController {
    private final UserDao userdao;
    private final JpaAlimentDao jpaAlimentDao;
    private final JpaPossederDao jpaPossederDao;
    private final AuthenticationService authService;

    @Autowired
    public UserRessourceController(UserDao userdao, JpaAlimentDao jpaAlimentDao, JpaPossederDao jpaPossederDao, AuthenticationService authService) {
        this.userdao = userdao;
        this.jpaAlimentDao = jpaAlimentDao;
        this.jpaPossederDao = jpaPossederDao;
        this.authService = authService;
    }

    /**
     * Get all users.
     *
     * @return a list of users
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userdao.getAll());
    }

    /**
     * Get a user by id.
     *
     * @param login the user id
     * @return the user
     */
    @GetMapping(value = "/{login}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getUser(@PathVariable("login") final String login) {
        Optional<User> user = userdao.findByLogin(login);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
    }

    /**
     * Update a user.
     *
     * @param id          the user id
     * @param requestBody the request body
     * @param contentType the content type
     * @return the response entity
     */
    @PutMapping(value = "/{login}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> updateUser(
            @PathVariable("login") final String id,
            @RequestBody String requestBody,
            @RequestHeader("Content-Type") String contentType) {
        try {
            Optional<UserRequestDto> requestDto = getUserDtoRequest(requestBody, contentType);
            if (requestDto.isPresent()) {
                Optional<User> user = userdao.findByLogin(id);
                if (user.isPresent()) {
                    UserRequestDto userdto = requestDto.get();
                    userdao.update(user.get(), new String[]{userdto.getName(), authService.encoderPassword(userdto.getPassword())});
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
            Optional<User> user = userdao.get(id);
            if (user.isPresent()) {
                userdao.delete(user.get());
                return ResponseEntity.ok("Utilisateur supprimé");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la suppression de l'utilisateur");
        }
    }

    @GetMapping(value = "/aliments",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAlimentsUser(@RequestParam("login") final String login) {
        Optional<User> user = userdao.findByLogin(login);
        if (user.isPresent()) {
            return ResponseEntity.ok(jpaPossederDao.findAlimentsByUser(user.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
    }

    @PostMapping(value = "/aliments",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<AuthenticationResponse> addEc(@RequestBody String requestBody, @RequestHeader("Content-Type") String contentType) throws JsonProcessingException {
        Optional<AddEcRequestDto> addEcRequestDto = getAddEcDtoRequest(requestBody, contentType);
        if (addEcRequestDto.isPresent()) {
            Optional<User> user = userdao.findByLogin(addEcRequestDto.get().getLogin());
            Optional<Aliment> aliment = jpaAlimentDao.get(addEcRequestDto.get().getAlimentId());

            if (user.isPresent() && aliment.isPresent()) {
                Posseder posseder = new Posseder(user.get(), aliment.get(), addEcRequestDto.get().getQuantity());
                jpaPossederDao.save(posseder);
                return ResponseEntity.ok(new AuthenticationResponse("Ajout effectué"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}

