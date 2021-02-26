package com.company.simulator.controller;

import com.company.simulator.model.StudentQuery;
import com.company.simulator.model.Task;
import com.company.simulator.repos.StudentQueryRepo;
import com.company.simulator.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class TaskController {

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("/task")
    public String task(Model model) {
        return "task";
    }

    @GetMapping("/task/all")
    public String taskList(Model model) {
        return "taskList";
    }

    @GetMapping(value = "/task/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTask(@PathVariable("id") Task task) {
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/task/create")
    public String createTask(Model model) {
        return "createTask";
    }

    @PostMapping("/task/create")
    public String addTask(@ModelAttribute Task task) {
        taskRepo.save(task);
        return ("task");
    }

    /*
        From Alexander
     */

    @Autowired
    private StudentQueryRepo queryRepo;

    @GetMapping("practice/task/{task}")
    public String taskById(
            @PathVariable Task task,
            Model model
    ) {
        model.addAttribute("task", task);
        return "practice/taskExecution";
    }

    @PostMapping("practice/task/{task}")
    public String saveStudentQuery(
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
