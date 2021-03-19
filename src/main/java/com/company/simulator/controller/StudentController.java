package com.company.simulator.controller;

import com.company.simulator.model.Student;
import com.company.simulator.model.StudentPK;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.StudentRepo;
import com.company.simulator.repos.TeamRepo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("/team")
    public String registerToTeam(
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        Model model
    ) {
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teamInvitation";
    }

    @PostMapping("/team")
    public String addStudentToTeam(
        @AuthenticationPrincipal User user,
        @RequestParam String invitation,
        RedirectAttributes redirectAttributes
    ) {
        final Optional<Team> team = teamRepo.findTeamByInvitation(invitation);
        team.ifPresentOrElse(
            group -> {
                Student student = new Student(
                    new StudentPK(user.getId(), group.getId()),
                    user,
                    group
                );
                studentRepo.save(student);
                redirectAttributes.addAttribute("message", "Successfully joined");
                redirectAttributes.addAttribute("type", "success");
            },
            () -> {
                redirectAttributes.addAttribute(
                    "message",
                    String.format("No team was found by invitation '%s'", invitation)
                );
                redirectAttributes.addAttribute("type", "danger");
            }
        );
        return "redirect:/student/team";
    }
}
