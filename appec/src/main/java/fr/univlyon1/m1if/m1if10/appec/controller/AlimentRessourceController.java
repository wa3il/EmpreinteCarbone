package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for aliment resource.
 * This controller is used to manage aliment resources.
 */
@RestController
@RequestMapping("/aliments")
public class AlimentRessourceController {

    @Autowired
    private Dao<Aliment> jpaAlimentDao;

    /**
     * Get all aliments.
     *
     * @return a list of aliments
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllAliment() {
        return ResponseEntity.ok(jpaAlimentDao.getAll());
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAliment(@PathVariable("id") final Integer id) {
        Optional<Aliment> aliment = jpaAlimentDao.get(id);
        if(aliment.isPresent()){
            return ResponseEntity.ok(aliment.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
    }

}
