package com.company.simulator.controller;

import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public String task(Model model) {
        return "task";
    }

    @GetMapping("/all")
    public String taskList(Model model) {
        return "taskList";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTask(@PathVariable("id") Long taskId) {
        if (taskId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Task> task = taskService.getById(taskId);

        if (!task.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/create")
    public String createTask(Model model) {
        return "createTask";
    }

    @PostMapping("/create")
    public String addTask(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String name,
            @RequestParam String correctQuery,
            @RequestParam Integer points,
            @RequestParam Long categoryId,
            Model model) {
        Task task = new Task(user.getId(), text, name, correctQuery, points, Boolean.TRUE, categoryId);
        taskService.save(task);
        return ("task");
    }
}
