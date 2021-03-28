package com.company.simulator.controller;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import com.company.simulator.sql.SqlTransaction;
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

    @GetMapping("/task")
    public String task(Model model) {
        return "practice/task";
    }

    @GetMapping("practice/{practice}/task/{task}")
    public String taskById(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @PathVariable Task task,
        @RequestParam(required = false, name = "sentQuery") String query,
        @RequestParam(required = false) String result,
        @RequestParam(required = false) String type,
        RedirectAttributes redirAttr,
        Model model
    ) {
        if (!practice.getId().equals(Practice.COMMON_POOL)
                && submRepo.findByUserAndPracticeAndTask(user, practice, task).isPresent()
        ) {
            redirAttr.addAttribute(
                "result",
                String.format("You have already solved this task `%s`", task.getName())
            );
            redirAttr.addAttribute("type", "warning");
            return String.format("redirect:/practice/%d", practice.getId());
        } else {
            model.addAttribute("task", task);
            model.addAttribute("sentQuery", query);
            model.addAttribute("result", result);
            model.addAttribute("type", type);
            return "practice/taskExecution";
        }
    }

    @PostMapping("practice/{practice}/task/{task}")
    public String saveSubmission(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @PathVariable Task task,
        @RequestParam(name = "query") String query,
        RedirectAttributes redirAttr,
        Model model
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
            redirAttr.addAttribute("result", "Query was successfully submitted");
            redirAttr.addAttribute("type", "success");
            return String.format("redirect:/practice/%d", practice.getId());
        } else if (res.getSqlException().isPresent()) {
            model.addAttribute("result", res.getSqlException().get());
        } else if (res.getInternalError().isPresent()) {
            model.addAttribute("result", res.getInternalError().get());
        }
        model.addAttribute("sentQuery", query);
        model.addAttribute("type", "danger");
        return "practice/taskExecution";
    }
}
