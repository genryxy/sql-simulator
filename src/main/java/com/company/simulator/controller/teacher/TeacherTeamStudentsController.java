package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/team")
public class TeacherTeamStudentsController {

    @Autowired
    TeamRepo teamRepo;

    @Autowired
    PracticeRepo practiceRepo;

    @GetMapping("/{practice}")
    public String teamsByPractice(Model model,
                                  @PathVariable Practice practice) {
        final Optional<List<Team>> teams = teamRepo.findTeamsByPracticesContains(practice);
        if (teams.isPresent()) {
            model.addAttribute("teams", teams.get());
        } else {
            model.addAttribute("teams", new ArrayList<Team>());
        }
        model.addAttribute("practiceId", practice.getId());
        return "teacher/teamsList";
    }

    @GetMapping("/{practice}/notIncludedTeams")
    public String notIncludedTeams(Model model,
                                   @PathVariable Practice practice,
                                   @AuthenticationPrincipal User user) {
        System.out.println(practice);
        final Optional<List<Team>> teams = teamRepo.findTeamsByPracticesNotContainsAndAuthorId(practice, user.getId());
        if (teams.isPresent()) {
            model.addAttribute("teams", teams.get());
        } else {
            model.addAttribute("teams", new ArrayList<Team>());
        }
        model.addAttribute("practiceId", practice.getId());
        return "teacher/addTeams";
    }

    @GetMapping("/{practiceId}/create")
    public String createTeam(Model model,
                             @PathVariable Long practiceId) {
        model.addAttribute("practiceId", practiceId);
        return "teacher/createTeam";
    }

    @PostMapping("/{practiceId}/create")
    public String saveTeam(@PathVariable Long practiceId,
                           @ModelAttribute Team team
    ) {
        teamRepo.save(team);
        return String.format("redirect:/teacher/team/%d/notIncludedTeams", practiceId);
    }

    @PostMapping("/assign")
    public String assignTeams(@RequestParam Long practiceId,
                              @RequestParam Long teamId
    ) {
        teamRepo.assignPracticeToTeam(practiceId, teamId);
        return String.format("redirect:/teacher/team/%d/notIncludedTeams", practiceId);
    }
}
