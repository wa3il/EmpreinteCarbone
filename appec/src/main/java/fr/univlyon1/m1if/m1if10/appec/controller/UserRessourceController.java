package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaAlimentDao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaPossederDao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaUserDao;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AddEcRequestDto;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import fr.univlyon1.m1if.m1if10.appec.model.Posseder;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static fr.univlyon1.m1if.m1if10.appec.controller.Mapdata.getAddEcDtoRequest;
import static fr.univlyon1.m1if.m1if10.appec.controller.Mapdata.getUserDtoRequest;

/**
 * Controller for user resource.
 * This controller is used to manage user resources.
 */
@RestController
@RequestMapping("/users")
public class UserRessourceController {
    private final JpaUserDao jpaUserDao;
    private final JpaAlimentDao jpaAlimentDao;
    private final JpaPossederDao jpaPossederDao;
    private final AuthenticationService authService;

    private static final String USER_NOT_FOUND_MESSAGE = "Utilisateur non trouvé";


    @Autowired
    public UserRessourceController(@Qualifier("jpaUserDao") JpaUserDao jpaUserDao, JpaAlimentDao jpaAlimentDao, JpaPossederDao jpaPossederDao, AuthenticationService authService) {
        this.jpaUserDao = jpaUserDao;
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
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(jpaUserDao.getAll());
    }

    /**
     * Get a user by id.
     *
     * @param login the user id
     * @return the user
     */
    @GetMapping(value = "/{login}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getUser(@PathVariable("login") final String login) {
        Optional<User> user = jpaUserDao.findByLogin(login);
        return user.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND_MESSAGE));
    }

    /**
     * Update a user.
     *
     * @param login       the user id
     * @param requestBody the request body
     * @param contentType the content type
     * @return the response entity
     */
    @PutMapping(value = "/{login}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> updateUser(
            @PathVariable("login") final String login,
            @RequestBody String requestBody,
            @RequestHeader("Content-Type") String contentType) {
        try {
            Optional<UserRequestDto> requestDto = getUserDtoRequest(requestBody, contentType);
            if (requestDto.isPresent()) {
                Optional<User> user = jpaUserDao.findByLogin(login);
                if (user.isPresent()) {
                    UserRequestDto userdto = requestDto.get();
                    if (userdto.getPassword().isBlank()) {
                        jpaUserDao.update(user.get(), new String[]{userdto.getName(), ""});
                    } else {
                        jpaUserDao.update(user.get(), new String[]{userdto.getName(), authService.encoderPassword(userdto.getPassword())});
                    }
                    return ResponseEntity.ok("Utilisateur mis à jour");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND_MESSAGE);
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
     * @param login the user id
     * @return the response entity
     */
    @DeleteMapping(value = "/{login}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> deleteUser(@PathVariable("login") final String login) {
        try {
            Optional<User> user = jpaUserDao.findByLogin(login);
            if (user.isPresent()) {
                jpaUserDao.delete(user.get());
                return ResponseEntity.ok("Utilisateur supprimé");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND_MESSAGE);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la suppression de l'utilisateur");
        }
    }

    @GetMapping(value = "/aliments",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Posseder>> getAlimentsUser(@RequestParam("login") final String login, @RequestParam(value = "date", required = false) String date) {
        Optional<User> user = jpaUserDao.findByLogin(login);
        if (user.isPresent()) {
            if (date != null && !date.isEmpty()) {
                return ResponseEntity.ok(jpaPossederDao.findAlimentsByUserAndDate(user.get(), Date.valueOf(date)));
            } else {
                return ResponseEntity.ok(jpaPossederDao.findAlimentsByUser(user.get()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(value = "/aliments",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<AuthenticationResponse> addEc(@RequestBody String requestBody, @RequestHeader("Content-Type") String contentType) throws JsonProcessingException {
        Optional<AddEcRequestDto> addEcRequestDto = getAddEcDtoRequest(requestBody, contentType);
        if (addEcRequestDto.isPresent()) {
            Optional<User> user = jpaUserDao.findByLogin(addEcRequestDto.get().getLogin());
            Optional<Aliment> aliment = jpaAlimentDao.get(addEcRequestDto.get().getAlimentId());

            if (user.isPresent() && aliment.isPresent()) {
                Posseder posseder = new Posseder();
                posseder.setUser(user.get());
                posseder.setAliment(aliment.get());
                posseder.setQuantity(addEcRequestDto.get().getQuantity());
                if (addEcRequestDto.get().getDate() == null) {
                    posseder.setDate(new Date(System.currentTimeMillis()));
                } else {
                    posseder.setDate(addEcRequestDto.get().getDate());
                }
                jpaPossederDao.save(posseder);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}

