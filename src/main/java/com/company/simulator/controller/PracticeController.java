package com.company.simulator.controller;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.SubmissionRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @GetMapping("/practice")
    public String allPractices(Model model) {
        final Iterable<Practice> practices = practiceRepo.findAll();
        model.addAttribute("practices", practices);
        return "practice/practiceList";
    }

    @GetMapping("/practice/{practice}")
    public String tasksByPracticeId(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @RequestParam(required = false) String result,
        @RequestParam(required = false) String type,
        Model model
    ) {
        final List<Task> tasks = tasksWithMarkedStatus(practice, user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("practice", practice);
        model.addAttribute("result", result);
        model.addAttribute("type", type);
        return "practice/taskList";
    }

    private List<Task> tasksWithMarkedStatus(Practice practice, User user) {
        final List<Task> tasks = new ArrayList<>(practice.getTasks());
        final Optional<List<Submission>> subms;
        subms = submRepo.findByUserAndPractice(user, practice);
        if (subms.isPresent()) {
            final Map<Long, Submission> tmp = subms.get().stream()
                .collect(
                    Collectors.toMap(
                        item -> item.getTask().getId(),
                        Function.identity(),
                        (dupl1, dupl2) -> dupl2
                    )
                );
            tasks.stream()
                .filter(
                    task -> tmp.containsKey(task.getId())
                ).forEach(
                task -> {
                    if (tmp.get(task.getId()).isCorrect()) {
                        task.setState(Task.Status.CORRECT_SOLVED);
                    } else {
                        task.setState(Task.Status.WRONG_SOLVED);
                    }
                }
            );
        }
        return tasks;
    }
}
