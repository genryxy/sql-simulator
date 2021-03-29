package com.company.simulator.controller;

import com.company.simulator.model.Category;
import com.company.simulator.model.Practice;
import com.company.simulator.model.Student;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.CategoryRepo;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.StudentRepo;
import com.company.simulator.repos.SubmissionRepo;
import com.company.simulator.repos.TaskRepo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
    private SubmissionRepo submRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("/practice")
    public String availablePractices(
        Model model,
        @RequestParam(required = false) Team team,
        @RequestParam(required = false) String result,
        @RequestParam(required = false) String type
    ) {
        final Iterable<Practice> practices;
        if (team == null) {
            practices = practiceRepo.findAllByIdIsNot(Practice.COMMON_POOL);
        } else {
            practices = team.getPractices().stream()
                .filter(prac -> !prac.getId().equals(Practice.COMMON_POOL))
                .collect(Collectors.toList());
        }
        model.addAttribute("practices", practices);
        model.addAttribute("result", result);
        model.addAttribute("type", type);
        return "practice/practiceList";
    }

    @GetMapping("/practice/{practice}")
    public String tasksByPracticeId(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @RequestParam(required = false) Category category,
        @RequestParam(required = false) String task_status,
        @RequestParam(required = false) String result,
        @RequestParam(required = false) String type,
        Model model
    ) {
        if (hasAccess(user, practice)) {
            final Collection<Task> tasks;
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("practice", practice);
            model.addAttribute("result", result);
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
            final List<Task> markedTasks = tasksWithMarkedStatus(tasks, practice, user);
            model.addAttribute("tasks", filterTasksByStatus(markedTasks, task_status));
            return template;
        } else {
            model.addAttribute(
                "result",
                String.format("Access to practice `%d` denied", practice.getId())
            );
            model.addAttribute("type", "danger");
            model.addAttribute("practices", practiceRepo.findAllByIdIsNot(Practice.COMMON_POOL));
            return "practice/practiceList";
        }
    }

    private boolean hasAccess(User user, Practice practice) {
        boolean allowed = false;
        final Set<Team> teams = practice.getTeams();
        final List<Student> students = studentRepo.findAllByUserId(user.getId())
            .orElseGet(ArrayList::new);
        for (Student student: students) {
            if (teams.contains(student.getTeam())) {
                allowed = true;
                break;
            }
        }
        return allowed;
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

    private List<Task> tasksWithMarkedStatus(Collection<Task> tasks, Practice practice, User user) {
        final List<Task> ctasks = new ArrayList<>(tasks);
        final Optional<List<Submission>> subms;
        subms = submRepo.findByUserAndPractice(user, practice);
        if (subms.isPresent()) {
            final Map<Long, Submission> submsMap = subms.get().stream()
                .collect(
                    Collectors.toMap(
                        item -> item.getTask().getId(),
                        Function.identity(),
                        (dupl1, dupl2) -> dupl2
                    )
                );
            ctasks.stream()
                .filter(
                    task -> submsMap.containsKey(task.getId())
                ).forEach(
                task -> {
                    if (submsMap.get(task.getId()).isCorrect()) {
                        task.setState(Task.Status.CORRECT_SOLVED);
                    } else {
                        task.setState(Task.Status.WRONG_SOLVED);
                    }
                }
            );
        }
        return ctasks;
    }
}
