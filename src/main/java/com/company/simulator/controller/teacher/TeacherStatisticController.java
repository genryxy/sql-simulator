package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teacher/statistic")
public class TeacherStatisticController {

    @Autowired
    private SubmissionRepo submissionRepo;

    @GetMapping("/{practice}")
    public String getStatistic(@AuthenticationPrincipal User user,
                               Model model,
                               @PathVariable Practice practice) {
        final List<Submission> submissions = submissionRepo.findByPractice(practice).orElseGet(ArrayList::new);
        model.addAttribute("submissions", submissions);
        return "teacher/statistic";
    }
}
