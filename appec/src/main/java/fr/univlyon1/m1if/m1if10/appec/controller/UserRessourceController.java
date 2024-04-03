package fr.univlyon1.m1if.m1if10.appec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRessourceController {

    @Autowired
    private Dao<User> jpaUserDao;

    @GetMapping
    public Iterable<User> get() {
        return jpaUserDao.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") final Integer id) {
        Optional<User> user = jpaUserDao.get(id);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
    }

    @PostMapping( consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createUser(@RequestBody User user) {

        // Vérifier si les paramètres de la requête sont valides
        if (user == null || user.getName() == null || user.getPassword() == null || user.getEmail() == null) {
            // Si les paramètres de la requête ne sont pas valides, renvoyer une réponse avec le code 400 et un message explicatif
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Paramètres de la requête non acceptables");
        }

        // Vérifie si un utilisateur avec le même login existe déjà
        for (User u : jpaUserDao.getAll()) {
            if (u.getEmail().equals(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Un utilisateur avec ce login existe déjà");
            }
        }

        // Logique pour créer un utilisateur
        try {
            jpaUserDao.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur créé.");
        } catch (Exception e) {
            // En cas d'erreur interne, renvoyer une réponse avec le code 500 (Erreur Interne du Serveur)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création de l'utilisateur.");
        }
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUser(@PathVariable("id") final Integer id, @RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getEmail().isEmpty() || user.getName() == null ||
                    user.getName().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("format de la requête non accepté");
            }

            Optional<User> userOptional = jpaUserDao.get(id);
            if (userOptional.isPresent()) {
                User u = userOptional.get();
                u.setName(user.getName());
                u.setPassword(user.getPassword());
                u.setEmail(user.getEmail());
                jpaUserDao.update(u, new String[]{u.getName(), u.getPassword(), u.getEmail()});
                return ResponseEntity.ok("Utilisateur mis à jour");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la mise à jour de l'utilisateur");
        }
    }


}
