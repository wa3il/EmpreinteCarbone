package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user business.
 * This controller is used to manage user connection and disconnection.
 */
@RequestMapping("/users")
@RestController
public class UserBusinessController {

    @Autowired
    private Dao<User> jpaUserDao;

    //@PostMapping("/create")

    //@PostMapping("/logout")

}
