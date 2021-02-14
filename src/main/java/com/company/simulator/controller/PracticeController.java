package com.company.simulator.controller;

import com.company.simulator.model.Message;
import com.company.simulator.model.Practice;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public final class PracticeController {

    @Autowired
    private PracticeRepo practiceRepo;

    @GetMapping("/practice")
    public String main(Model model) {
        final Iterable<Practice> practices = practiceRepo.findAll();
        model.addAttribute("practices", practices);
        return "practice/practice";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
        @AuthenticationPrincipal User currentUser,
        @PathVariable User user,
        Model model,
        @RequestParam(required = false) Message message
    ) {
        model.addAttribute("messages", user.getMessages());
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userMessages";
    }
}
