package com.company.simulator.controller.teacher;

import com.company.simulator.model.Task;
import com.company.simulator.repos.TaskRepo;
import com.company.simulator.sql.SqlTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/teacher")
public class TeacherTaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private SqlTransaction sqlTransaction;

    @GetMapping("task")
    public String task(Model model) {
        return "task";
    }

    @GetMapping("task/{task}")
    public String getTask(@PathVariable("task") Task task, Model model) {
        model.addAttribute("task", task);
        return "teacher/taskInfo";
    }

    @GetMapping("task/create")
    public String createTask(Model model,
                             @RequestParam(required = false) String message,
                             @RequestParam(required = false) String type
    ) {
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/createTask";
    }

    @PostMapping("task/create")
    public String addTask(@ModelAttribute Task task,
                          RedirectAttributes redirectAttributes
    ) {
        String message = "Successfully created",
            type = "success";
        try {
            sqlTransaction.validationTeacherQuery(task.getDdlScript(), task.getCorrectQuery());
            taskRepo.save(task);
        } catch (Exception e) {
            message = e.getMessage();
            type = "danger";
        }
        redirectAttributes.addAttribute("message", message);
        redirectAttributes.addAttribute("type", type);
        return ("redirect:/teacher/task/create");
    }

    @GetMapping("practice/{practice}/task/{task}")
    public String editTaskById(
        @PathVariable Task task,
        Model model
    ) {
        model.addAttribute("task", task);
        // TODO: Form for editing of tasks should be completed.
        // Probably the path can be "teacher/task/{task}" because one task
        // can be included in many practices.
        // For this purpose method for obtaining task by id
        // in JSON should be removed.
        return "teacher/taskEdit";
    }
}
