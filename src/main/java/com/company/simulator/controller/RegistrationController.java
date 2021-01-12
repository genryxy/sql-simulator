package com.company.simulator.controller;

import com.company.simulator.model.User;
import com.company.simulator.service.UserService;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
        @RequestParam("passwordConfirm") String passwordConfirm,
        @Valid User user,
        BindingResult bindingResult,
        Model model
    ) {
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute(
                "passwordConfirmError",
                "Password confirmation cannot be empty"
            );
        }
        if (!Objects.equals(user.getPassword(), passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "registration";
        }
        if (isConfirmEmpty || bindingResult.hasErrors()) {
            final Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        final boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User was successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code was not found");
        }
        return "login";
    }
}
