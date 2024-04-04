package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Aliments")
public class AlimentRessourceController {

    @Autowired
    private Dao<Aliment> jpaAlimentDao;

    @GetMapping
    public Iterable<Aliment> get() {
        return jpaAlimentDao.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") final Integer id) {
        Optional<Aliment> aliment = jpaAlimentDao.get(id);
        if(aliment.isPresent()) {
            return ResponseEntity.ok(aliment.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aliment non trouvé");
    }
}