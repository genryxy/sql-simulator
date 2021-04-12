package com.company.simulator.controller;

import com.company.simulator.model.User;
import com.company.simulator.repos.UserRepo;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("profile")
    public String getProfile(
        Model model,
        @AuthenticationPrincipal User user,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type
    ) {
        final User upd = userRepo.findById(user.getId()).orElse(user);
        model.addAttribute("user", upd);
        model.addAttribute("email", upd.getEmail());
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
        @AuthenticationPrincipal User user,
        @RequestParam String password,
        @RequestParam String email,
        RedirectAttributes redirAttr,
        Model model
    ) {
        if (StringUtils.isEmpty(password)) {
            model.addAttribute("passwordError", "Password cannot be empty");
        }
        if (StringUtils.isEmpty(email)) {
            model.addAttribute("emailError", "Email cannot be empty");
        }
        if (model.asMap().size() > 0) {
            model.addAttribute("user", user);
            model.addAttribute("email", email);
            return "profile";
        }
        userService.updateProfile(user, password, email);
        redirAttr.addAttribute("message", "Data was successfully updated");
        redirAttr.addAttribute("type", "success");
        return "redirect:/user/profile";
    }
}
