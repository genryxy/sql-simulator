package com.company.simulator.controller.result;

import com.company.simulator.exception.AccessDeniedException;
import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.Submission;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/result/submission")
public class ResultSubmissionController {
    @Autowired
    private SubmissionRepo submRepo;

    @GetMapping
    public String allSubmissions(
        @AuthenticationPrincipal User user,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        Model model
    ) {
        final List<Submission> subms = submRepo.findByUser(user).orElseGet(ArrayList::new);
        model.addAttribute("submissions", subms);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "result/resultSubmission";
    }

    @GetMapping("{subm}")
    public String submissionById(
        @AuthenticationPrincipal User user,
        @PathVariable Submission subm,
        Model model
    ) {
        if (subm == null) {
            throw new NotFoundException("There is no such submission");
        }
        if (!subm.getUser().equals(user)) {
            throw new AccessDeniedException(
                String.format("Access to submission `%d` denied", subm.getId())
            );
        }
        model.addAttribute("submission", subm);
        return "result/submissionInfo";
    }
}
