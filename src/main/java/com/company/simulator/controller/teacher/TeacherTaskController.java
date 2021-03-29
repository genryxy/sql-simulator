package com.company.simulator.controller.teacher;

import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.CategoryRepo;
import com.company.simulator.repos.TaskRepo;
import com.company.simulator.sql.SqlTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teacher")
public class TeacherTaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private SqlTransaction sqlTransaction;

    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("task")
    public String getAllTasks(@AuthenticationPrincipal User user,
                              Model model) {
        final Iterable<Task> tasks = taskRepo.findAllTaskByAuthorId(user.getId());
        model.addAttribute("tasks", tasks);
        return "teacher/task";
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
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/createTask";
    }

    @PostMapping("task/create")
    public String addTask(@ModelAttribute Task task,
                          RedirectAttributes redirectAttributes
    ) {
        try {
            sqlTransaction.validationTeacherQuery(task.getDdlScript(), task.getCorrectQuery());
            taskRepo.save(task);
            redirectAttributes.addAttribute("message", "Task successfully created");
            redirectAttributes.addAttribute("type", "success");
            return "redirect:/teacher/practice/create";
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/task/create";
        }
    }

    @GetMapping("task/{task}/edit")
    public String editTask(
        @PathVariable Task task,
        Model model
    ) {
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("task", task);
        return "teacher/taskEdit";
    }

    @PostMapping("task/{task}/edit")
    public String saveEditTask(
            @PathVariable Task task,
            Model model
    ) {
        model.addAttribute("task", task);
        return "/teacher/task";
    }
}
