package com.company.simulator.controller;

import com.company.simulator.model.Message;
import com.company.simulator.model.StudentQuery;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.StudentQueryRepo;
import com.company.simulator.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/practice")
public class TaskController {
    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private StudentQueryRepo queryRepo;

    @GetMapping("/task/{task}")
    public String taskById(
        @PathVariable Task task,
        Model model
    ) {
        model.addAttribute("task", task);
        return "practice/taskExecution";
    }

    @PostMapping("/task/{task}")
    public String updateMessage(
        @PathVariable Task task,
        @RequestParam(name = "query") String query,
        Model model
    ) {
        // TODO: Check student's answer. Is it correct query or wrong?
        final StudentQuery stq = new StudentQuery();
        stq.setCorrect(true);
        stq.setQuery(query);
        stq.setTask(task);
        queryRepo.save(stq);
        model.addAttribute("task", task);
        return String.format("redirect:/practice/task/%d", task.getId());
    }

}

