package com.company.simulator.controller;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import com.company.simulator.repos.PracticeRepo;
import java.util.ArrayList;
import java.util.List;
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
    public String allPractices(Model model) {
        final Iterable<Practice> practices = practiceRepo.findAll();
        model.addAttribute("practices", practices);
        return "practice/practiceList";
    }

    @GetMapping("/practice/{practice}")
    public String tasksByPracticeId(
        @PathVariable Practice practice,
        Model model
    ) {
        final List<Task> tasks = new ArrayList<>(practice.getTasks());
        model.addAttribute("tasks", tasks);
        return "practice/taskList";
    }
}
