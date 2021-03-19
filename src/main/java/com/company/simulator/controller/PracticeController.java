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
import org.springframework.web.bind.annotation.RequestParam;

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
        @RequestParam(required = false) String result,
        @RequestParam(required = false) String type,
        Model model
    ) {
        final List<Task> tasks = new ArrayList<>(practice.getTasks());
        model.addAttribute("tasks", tasks);
        model.addAttribute("practice", practice);
        model.addAttribute("result", result);
        model.addAttribute("type", type);
        return "practice/taskList";
    }
}
