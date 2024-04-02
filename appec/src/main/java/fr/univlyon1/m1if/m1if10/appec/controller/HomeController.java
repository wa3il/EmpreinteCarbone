package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaUserDao;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private Dao<User> jpaUserDao;

    @GetMapping("/")
    public String home(Model model) {
        long numberOfUsers = jpaUserDao.getAll().size();
        model.addAttribute("numberOfUsers", numberOfUsers);
        return "home";
    }


}
