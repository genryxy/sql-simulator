package com.company.simulator.controller.result;

import com.company.simulator.access.AccessStudent;
import com.company.simulator.exception.AccessDeniedException;
import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.Practice;
import com.company.simulator.model.ResultPractice;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.processing.TasksMarked;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.StudentRepo;
import com.company.simulator.repos.SubmissionRepo;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultPracticeController {
    @Autowired
    private SubmissionRepo submRepo;

    @Autowired
    private PracticeRepo practiceRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TasksMarked tasksMarked;

    @GetMapping("/result/practice")
    public String resultsForPractices(
        @AuthenticationPrincipal User user,
        Model model
    ) {
        final List<ResultPractice> resPracs;
        resPracs = practiceRepo.findAllForUserExcept(user.getId(), Practice.COMMON_POOL)
            .orElseGet(ArrayList::new).stream()
            .map(
                prac -> {
                    final Map<String, Number> res;
                    res = submRepo.findScoreToTotalForPracticeByUser(prac.getId(), user.getId());
                    return new ResultPractice(
                        prac,
                        res.get("total").intValue(),
                        res.get("score").intValue()
                    );
                }
            ).collect(Collectors.toList());
        model.addAttribute("results", resPracs);
        return "result/resultPractice";
    }

    @GetMapping("/result/practice/{practice}")
    public String resultsByPractice(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        Model model
    ) {
        if (practice == null) {
            throw new NotFoundException("There is no such practice");
        }
        if (!new AccessStudent(user, studentRepo).toPractice(practice)) {
            throw new AccessDeniedException(
                String.format(
                    "Access to practice id `%d` is denied",
                    practice.getId()
                )
            );
        }
        final List<Task> tasks = tasksMarked.markedStatus(practice.getTasks(), practice, user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("practice", practice);
        return "result/onePractice";
    }

    @GetMapping("/result/practice/{practice}/task/{task}")
    public String resultsByPracticeAndTask(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @PathVariable Task task,
        Model model
    ) {
        if (practice == null) {
            throw new NotFoundException("There is no such practice");
        }
        if (task == null) {
            throw new NotFoundException("There is no such task");
        }
        if (!new AccessStudent(user, studentRepo).toPractice(practice)) {
            throw new AccessDeniedException(
                String.format(
                    "Access to practice id `%d` is denied",
                    practice.getId()
                )
            );
        }
        model.addAttribute(
            "submissions",
            submRepo.findByUserAndPracticeAndTask(user, practice, task).orElseGet(ArrayList::new)
        );
        return "result/oneTaskFromPractice";
    }
}
