package com.company.simulator.controller;

import com.company.simulator.model.User;
import com.company.simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("profile")
    public String getProfile(
        Model model,
        @AuthenticationPrincipal User user,
        @RequestParam(required = false, defaultValue = "") String message
    ) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        if (message.length() > 0) {
            model.addAttribute("message", message);
        }
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
        @AuthenticationPrincipal User user,
        @RequestParam String password,
        @RequestParam String email,
        Model model
    ) {
        if (StringUtils.isEmpty(password)) {
            model.addAttribute("passwordError", "Password cannot be empty");
        }
        if (StringUtils.isEmpty(email)) {
            model.addAttribute("emailError", "Email cannot be empty");
        }
        if (model.asMap().size() > 0) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", email);
            return "profile";
        }
        userService.updateProfile(user, password, email);
        return "redirect:/user/profile?message=Data was successfully updated";
    }
}
