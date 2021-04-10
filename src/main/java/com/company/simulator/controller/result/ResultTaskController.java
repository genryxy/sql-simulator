package com.company.simulator.controller.result;

import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.ResultPractice;
import com.company.simulator.model.ResultTask;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import com.company.simulator.repos.TaskRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/result/task")
public class ResultTaskController {
    @Autowired
    private SubmissionRepo submRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping
    public String getAllAvailableTasks(
        @AuthenticationPrincipal User user,
        Model model
    ) {
        final List<ResultTask> resTasks;
        resTasks = taskRepo.findAllForUser(user.getId())
            .orElseGet(ArrayList::new).stream()
            .map(
                task -> {
                    final Map<String, Number> res;
                    res = submRepo.findNumberAttemptsForTaskAndUser(task.getId(), user.getId());
                    return new ResultTask(
                        task,
                        res.get("total").intValue(),
                        res.get("correct").intValue()
                    );
                }
            ).collect(Collectors.toList());
        model.addAttribute("results", resTasks);
        return "result/resultTask";
    }

    @GetMapping("{task}")
    public String submissionsByTask(
        @AuthenticationPrincipal User user,
        @PathVariable Task task,
        Model model
    ) {
        if (task == null) {
            throw new NotFoundException("There is no such task");
        }
        model.addAttribute(
            "submissions",
            submRepo.findByUserAndTask(user, task).orElseGet(ArrayList::new)
        );
        return "result/oneTask";
    }
}
