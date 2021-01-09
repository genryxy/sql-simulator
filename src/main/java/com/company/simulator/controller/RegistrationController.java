package com.company.simulator.controller;

import com.company.simulator.model.User;
import com.company.simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(final User user, Model model) {
        if (!userService.addUser(user)) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        final boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("message", "User was successfully activated");
        } else {
            model.addAttribute("message", "Activation code was not found");
        }
        return "login";
    }
}
