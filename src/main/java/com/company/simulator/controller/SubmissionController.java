package com.company.simulator.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("submission")
public class SubmissionController {
    @Autowired
    private SubmissionRepo submRepo;

    @GetMapping("")
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
        return "result/submission";
    }

    @GetMapping("{subm}")
    public String submissionById(
        @AuthenticationPrincipal User user,
        @PathVariable Submission subm,
        RedirectAttributes redirAttr,
        Model model
    ) {
        if (subm.getUser().equals(user)) {
            model.addAttribute("submission", subm);
            return "result/submissionInfo";
        } else {
            redirAttr.addAttribute(
                "message",
                String.format("Access to submission `%d` denied", subm.getId())
            );
            redirAttr.addAttribute("type", "danger");
            return "redirect:/submission";
        }
    }
}
