package com.company.simulator.controller;

import com.company.simulator.model.Role;
import com.company.simulator.model.User;
import com.company.simulator.repos.UserRepo;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userrepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(final User user, Map<String, Object> model) {
        final User userfromdb = userrepo.findByUsername(user.getUsername());
        if (userfromdb != null) {
            model.put("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userrepo.save(user);
        return "redirect:/login";
    }
}
