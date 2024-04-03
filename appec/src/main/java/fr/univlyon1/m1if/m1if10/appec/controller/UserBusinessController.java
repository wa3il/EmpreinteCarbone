package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserBusinessController {

    @Autowired
    private Dao<User> jpaUserDao;



}
