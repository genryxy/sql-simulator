package com.company.simulator.controller;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import com.company.simulator.repos.PracticeRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public final class PracticeController {
    @Autowired
    private PracticeRepo practiceRepo;

    @GetMapping("/practice")
    public String getAllPractices(Model model) {
        final Iterable<Practice> practices = practiceRepo.findAll();
        model.addAttribute("practices", practices);
        return "practice/practiceList";
    }

    @GetMapping("/practice/{id}")
    public String getTasksByPracticeId(
        @PathVariable Long id,
        Model model
    ) {
        final Optional<Practice> practice = practiceRepo.findById(id);
        final List<Task> tasks;
        if (practice.isPresent()) {
            tasks = new ArrayList<>(practice.get().getTasks());
        } else {
            throw new IllegalStateException(
                String.format("Failed to get practice by id `%d`", id)
            );
        }
        model.addAttribute("tasks", tasks);
        return "practice/taskList";
    }

//    @GetMapping("/user-messages/{user}")
//    public String userMessages(
//        @AuthenticationPrincipal User currentUser,
//        @PathVariable User user,
//        Model model,
//        @RequestParam(required = false) Message message
//    ) {
//        model.addAttribute("messages", user.getMessages());
//        model.addAttribute("message", message);
//        model.addAttribute("isCurrentUser", currentUser.equals(user));
//        return "userMessages";
//    }
}
