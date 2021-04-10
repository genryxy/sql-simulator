package com.company.simulator.controller;

import com.company.simulator.access.AccessStudent;
import com.company.simulator.exception.AccessDeniedException;
import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.StudentRepo;
import com.company.simulator.repos.SubmissionRepo;
import com.company.simulator.sql.SqlTransaction;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskController {
    @Autowired
    private SubmissionRepo submRepo;

    @Autowired
    private SqlTransaction sqlTransaction;

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("practice/{practice}/task/{task}")
    public String taskById(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @PathVariable Task task,
        @RequestParam(required = false, name = "sentQuery") String query,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        Model model
    ) {
        if (practice == null) {
            throw new NotFoundException("There is no such practice");
        }
        if (task == null) {
            throw new NotFoundException("There is no such task");
        }
        if (new AccessStudent(user, studentRepo).toPractice(practice)) {
            model.addAttribute("task", task);
            model.addAttribute("sentQuery", query);
            model.addAttribute(
                "submissions",
                submRepo.findByUserAndPracticeAndTask(user, practice, task).orElseGet(ArrayList::new)
            );
            final Map<String, Integer> attempts = submRepo.findNumberAttemptsForTask(task.getId());
            model.addAttribute("totalAttempts", attempts.get("total"));
            model.addAttribute("correctAttempts", attempts.get("correct"));
            model.addAttribute("message", message);
            model.addAttribute("type", type);
            return "practice/taskExecution";
        }
            throw new AccessDeniedException(
                String.format(
                    "Access to task id `%d` from practice id `%d` is denied",
                    task.getId(),
                    practice.getId()
                )
            );
    }

    @PostMapping("practice/{practice}/task/{task}")
    public String saveSubmission(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @PathVariable Task task,
        @RequestParam(name = "query") String query,
        RedirectAttributes redirAttr
    ) {
        final SqlTransaction.ResultQuery res;
        res = sqlTransaction.executeQuery(
            task.getDdlScript(), query, task.getCorrectQuery()
        ).getBody();
        if (res.getInternalError().isEmpty() && res.getSqlException().isEmpty()) {
            final Submission subm = new Submission(query, res.isCorrect(), task);
            subm.setPractice(practice);
            subm.setUser(user);
            submRepo.save(subm);
            redirAttr.addAttribute("message", "Query was successfully submitted");
            redirAttr.addAttribute("type", "success");
        } else {
            addAttributesAboutResult(redirAttr, res);
        }
        redirAttr.addAttribute("sentQuery", query);
        redirAttr.addAttribute(
            "submissions",
            submRepo.findByUserAndPracticeAndTask(user, practice, task).orElseGet(ArrayList::new)
        );
        return String.format("redirect:/practice/%d/task/%d", practice.getId(), task.getId());
    }

    private void addAttributesAboutResult(Model model, SqlTransaction.ResultQuery res) {
        if (res.getSqlException().isPresent()) {
            model.addAttribute("message", res.getSqlException().get());
        } else if (res.getInternalError().isPresent()) {
            model.addAttribute("message", res.getInternalError().get());
        }
        model.addAttribute("type", "danger");
    }
}
