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
                              @RequestParam(required = false) String message,
                              @RequestParam(required = false) String type,
                              Model model
    ) {
        final Iterable<Task> tasks = taskRepo.findAllTaskByAuthorId(user.getId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/task";
    }

    @GetMapping("task/{task}/info")
    public String getTask(@PathVariable("task") Task task,
                          @AuthenticationPrincipal User user,
                          @RequestParam(required = false) String message,
                          @RequestParam(required = false) String type,
                          RedirectAttributes redirectAttributes,
                          Model model
    ) {
        try {
            if (user.equals(task.getAuthor())) {
                model.addAttribute("task", task);
                model.addAttribute("message", message);
                model.addAttribute("type", type);
                return "teacher/taskInfo";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/task";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such task");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/task";
        }
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
            if (task.getIsPrivate() == null) {
                task.setIsPrivate(false);
            }
            sqlTransaction.validationTeacherQuery(task.getDdlScript(), task.getCorrectQuery());
            taskRepo.save(task);
            redirectAttributes.addAttribute("message", "Task successfully created");
            redirectAttributes.addAttribute("type", "success");
            return "redirect:/teacher/task";
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/task/create";
        }
    }

    @GetMapping("task/{task}/edit")
    public String editTask(
        @PathVariable Task task,
        @AuthenticationPrincipal User user,
        Model model,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        RedirectAttributes redirectAttributes
    ) {
        try {
            if (user.equals(task.getAuthor())) {
                model.addAttribute("categories", categoryRepo.findAll());
                model.addAttribute("task", task);
                model.addAttribute("message", message);
                model.addAttribute("type", type);
                return "teacher/taskEdit";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/task";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such task");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/task";
        }
    }

    @PostMapping("task/{task}/edit")
    public String saveEditTask(
        @PathVariable Task task,
        @AuthenticationPrincipal User user,
        @ModelAttribute Task editedTask,
        RedirectAttributes redirectAttributes
    ) {
        if (user.equals(task.getAuthor())) {
            try {
                sqlTransaction.validationTeacherQuery(editedTask.getDdlScript(), editedTask.getCorrectQuery());
                taskRepo.updateTask(task.getId(),
                                    editedTask.getAuthor().getId(),
                                    editedTask.getName(),
                                    editedTask.getText(),
                                    editedTask.getDdlScript(),
                                    editedTask.getCorrectQuery(),
                                    editedTask.getPoints(),
                                    editedTask.getIsPrivate(),
                                    editedTask.getCategory().getId());
                redirectAttributes.addAttribute("message", "Task successfully edited");
                redirectAttributes.addAttribute("type", "success");
                return String.format("redirect:/teacher/task/%d/info", task.getId());
            } catch (Exception e) {
                redirectAttributes.addAttribute("message", e.getMessage());
                redirectAttributes.addAttribute("type", "danger");
                return String.format("redirect:/teacher/task/%d/edit", task.getId());
            }
        }
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
        return "redirect:/teacher/task";
    }

    @PostMapping("task/{task}/remove")
    public String removeTask(
        @PathVariable Task task,
        @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes
    ) {
        if (user.equals(task.getAuthor())) {
            taskRepo.delete(task);
            return "redirect:/teacher/task";
        }
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
        return "redirect:/teacher/task";
    }
}
