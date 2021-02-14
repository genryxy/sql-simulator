package com.company.simulator.controller;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import com.company.simulator.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TaskController {
    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("/practice/task/{id}")
    public String getAllPractices(
        Model model
    ) {
        final Iterable<Task> task = taskRepo.findAll();
        model.addAttribute("tasks", task);
//        return "practice/practiceList";
        return null;
    }

}

