package com.company.simulator.controller.admin;

import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.Role;
import com.company.simulator.model.User;
import com.company.simulator.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin/user")
public class AdminUsersController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        if (user == null) {
            throw new NotFoundException("There is no such user");
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "admin/userEdit";
    }

    @PostMapping
    public String userSave(
        @RequestParam String username,
        @RequestParam Map<String, String> form,
        @RequestParam("userId") User user
    ) {
        userService.saveUser(user, username, form);
        return "redirect:/admin/user";
    }
}
