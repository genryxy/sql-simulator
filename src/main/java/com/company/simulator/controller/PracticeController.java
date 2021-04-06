package com.company.simulator.controller;

import com.company.simulator.access.AccessStudent;
import com.company.simulator.model.Category;
import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.processing.PracticeFilter;
import com.company.simulator.processing.TasksMarked;
import com.company.simulator.repos.CategoryRepo;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.StudentRepo;
import com.company.simulator.repos.TaskRepo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public final class PracticeController {
    @Autowired
    private PracticeRepo practiceRepo;

    @Autowired
    private TasksMarked tasksMarked;

    @Autowired
    private PracticeFilter pracFilter;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("/practice")
    public String availablePractices(
        @AuthenticationPrincipal User user,
        @RequestParam(required = false) Team team,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        RedirectAttributes redirAttr,
        Model model
    ) {
        final List<Practice> practices;
        if (team == null) {
            practices = practiceRepo.findAllForUserExcept(user.getId(), Practice.COMMON_POOL)
                .orElseGet(ArrayList::new);
        } else {
            if (new AccessStudent(user, studentRepo).toTeam(team)) {
                practices = team.getPractices().stream()
                    .filter(prac -> !prac.getId().equals(Practice.COMMON_POOL))
                    .collect(Collectors.toList());
            } else {
                return redirectToPractice(
                    redirAttr,
                    String.format("Access to practices for team `%d` is denied", team.getId())
                );
            }
        }
        model.addAttribute("practices", pracFilter.filterByStatus(practices, status));
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "practice/practiceList";
    }

    @GetMapping("/practice/{practice}")
    public String tasksByPracticeId(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @RequestParam(required = false) Category category,
        @RequestParam(required = false) String task_status,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        RedirectAttributes redirAttr,
        Model model
    ) {
        if (new AccessStudent(user, studentRepo).toPractice(practice)) {
            final Collection<Task> tasks;
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("practice", practice);
            model.addAttribute("message", message);
            model.addAttribute("type", type);
            final String template;
            if (practice.getId().equals(Practice.COMMON_POOL)) {
                if (category != null) {
                    tasks = taskRepo.findAllByCategoryAndPractice(category.getId(), practice.getId());
                    model.addAttribute("category_filter", category);
                } else {
                    tasks = practice.getTasks();
                }
                template = "practice/commonPool";
            } else {
                tasks = practice.getTasks();
                template = "practice/tasksByPractice";
            }
            final List<Task> markedTasks = tasksMarked.markedStatus(tasks, practice, user);
            model.addAttribute("tasks", filterTasksByStatus(markedTasks, task_status));
            return template;
        } else {
            return redirectToPractice(
                redirAttr,
                String.format("Access to practice `%d` is denied", practice.getId())
            );
        }
    }

    private String redirectToPractice(RedirectAttributes redirAttr, String msg) {
        redirAttr.addAttribute("message", msg);
        redirAttr.addAttribute("type", "danger");
        return "redirect:/practice";
    }

    private Collection<Task> filterTasksByStatus(Collection<Task> tasks, String status) {
        final Collection<Task> res;
        if (status != null) {
            res = tasks.stream().filter(
                task -> task.getState().value().equals(status)
            ).collect(Collectors.toList());
        } else {
            res = tasks;
        }
        return res;
    }

}