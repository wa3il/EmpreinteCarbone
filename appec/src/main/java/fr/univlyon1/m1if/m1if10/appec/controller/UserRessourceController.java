package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
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

import static fr.univlyon1.m1if.m1if10.appec.controller.Mapdata.extractFormData;

/**
 * Controller for user resource.
 * This controller is used to manage user resources.
 */
@RestController
@RequestMapping("/users")
public class UserRessourceController {

    @Autowired
    private Dao<User> jpaUserDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Get all users.
     *
     * @return a list of users
     */
    @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(jpaUserDao.getAll());
    }

    /**
     * Get a user by id.
     *
     * @param id the user id
     * @return the user
     */
    @GetMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getUser(@PathVariable("id") final Integer id) {
        Optional<User> user = jpaUserDao.get(id);
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
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> createUser(@RequestBody String requestBody, @RequestHeader("Content-Type") String contentType) {
        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                User user = objectMapper.readValue(requestBody, User.class);
                if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Tous les paramètres sont requis.");
                }

                jpaUserDao.save(user);
                return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur créé.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la lecture des données JSON.");
            }
        } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            try {
                Map<String, String> formData = extractFormData(requestBody);
                String name = formData.get("name");
                String email = formData.get("email");
                String password = formData.get("password");
                if (name == null || email == null || password == null) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Tous les paramètres sont requis.");
                }
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                jpaUserDao.save(user);
                return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur créé.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la lecture des données URL encodées.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Type de média non pris en charge.");
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
            if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                // Si le type de contenu est JSON
                User user = objectMapper.readValue(requestBody, User.class);
                // Vérifier si les paramètres requis sont présents
                if (user.getEmail() == null || user.getEmail().isEmpty() || user.getName() == null ||
                        user.getName().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Tous les paramètres sont requis.");
                }
                jpaUserDao.update(user, new String[]{user.getName(), user.getPassword(), user.getEmail()});
                return ResponseEntity.status(HttpStatus.OK).body("Utilisateur mis à jour.");

            } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                // Si le type de contenu est URL encodé
                Map<String, String> formData = extractFormData(requestBody);
                String name = formData.get("name");
                String email = formData.get("email");
                String password = formData.get("password");
                // Vérifier si les paramètres requis sont présents
                if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Tous les paramètres sont requis.");
                }
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                jpaUserDao.update(user, new String[]{name, password, email});
                return ResponseEntity.status(HttpStatus.OK).body("Utilisateur mis à jour.");

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
            Optional<User> user = jpaUserDao.get(id);
            if (user.isPresent()) {
                jpaUserDao.delete(user.get());
                return ResponseEntity.ok("Utilisateur supprimé");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la suppression de l'utilisateur");
        }
    }
}
