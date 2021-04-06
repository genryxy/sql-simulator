package com.company.simulator.controller;

import com.company.simulator.access.AccessStudent;
import com.company.simulator.exception.AccessDeniedException;
import com.company.simulator.exception.NotFoundException;
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
import com.company.simulator.repos.TeamRepo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private TeamRepo teamRepo;

    @GetMapping("/practice")
    public String availablePractices(
        @AuthenticationPrincipal User user,
        @RequestParam(required = false, name = "team") Long teamId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        Model model
    ) {
        final List<Practice> practices;
        if (teamId == null) {
            practices = practiceRepo.findAllForUserExcept(user.getId(), Practice.COMMON_POOL)
                .orElseGet(ArrayList::new);
        } else {
            final Optional<Team> team = teamRepo.findById(teamId);
            if (team.isEmpty()) {
                throw new NotFoundException("There is no such team");
            }
            if (!new AccessStudent(user, studentRepo).toTeam(team.get())) {
                throw new AccessDeniedException(
                    String.format("Access to practices for team id `%d` is denied", teamId)
                );
            }
            practices = team.get().getPractices().stream()
                .filter(prac -> !prac.getId().equals(Practice.COMMON_POOL))
                .collect(Collectors.toList());
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
        Model model
    ) {
        if (practice == null) {
            throw new NotFoundException("There is no such practice");
        }
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
        }
        throw new AccessDeniedException(
            String.format("Access to practice with id `%d` is denied", practice.getId())
        );
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