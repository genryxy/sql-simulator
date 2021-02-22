package com.company.simulator.controller;

import com.company.simulator.model.StudentQuery;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.StudentQueryRepo;
import com.company.simulator.repos.TaskRepo;
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
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/task")
    public String task(Model model) {
        return "task";
    }

    @GetMapping("/task/all")
    public String taskList(Model model) {
        return "taskList";
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/task/create")
    public String createTask(Model model) {
        return "createTask";
    }

    @PostMapping("/task/create")
    public String addTask(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String name,
            @RequestParam String ddlScript,
            @RequestParam String correctQuery,
            @RequestParam Integer points,
            @RequestParam Long categoryId,
            Model model) {
        Task task = new Task(user.getId(), text, name, ddlScript, correctQuery, points, Boolean.TRUE, categoryId);
        taskService.save(task);
        return ("task");
    }

    /*
        From Alexander
     */

    @Autowired
    private StudentQueryRepo queryRepo;

    @GetMapping("practice//task/{task}")
    public String taskById(
            @PathVariable Task task,
            Model model
    ) {
        model.addAttribute("task", task);
        return "practice/taskExecution";
    }

    @PostMapping("practice//task/{task}")
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
