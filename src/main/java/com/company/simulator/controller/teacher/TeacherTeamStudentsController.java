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

    @GetMapping("/{practiceId}")
    public String teamsByPractice(Model model,
                                  @PathVariable Long practiceId) {
        final Optional<Practice> practice = practiceRepo.findById(practiceId);
        if (practice.isPresent()) {
            final Optional<List<Team>> teams = teamRepo.findTeamsByPracticesContains(practice.get());
            if (teams.isPresent()) {
                model.addAttribute("teams", teams.get());
            } else {
                model.addAttribute("teams", new ArrayList<Team>());
            }
            model.addAttribute("practiceId", practiceId);
            return "teacher/teamsList";
        }
        return "teacher";
    }

    @GetMapping("/{practiceId}/notIncludedTeams")
    public String notIncludedTeams(Model model,
                                   @PathVariable Long practiceId,
                                   @AuthenticationPrincipal User user) {
        final Optional<Practice> practice = practiceRepo.findById(practiceId);
        if (practice.isPresent()) {
            final Optional<List<Team>> teams = teamRepo.findTeamsByPracticesNotContainsAndAuthorId(practice.get(), user.getId());
            if (teams.isPresent()) {
                model.addAttribute("teams", teams.get());
            } else {
                model.addAttribute("teams", new ArrayList<Team>());
            }
            model.addAttribute("practiceId", practiceId);
            return "teacher/addTeams";
        }
        return "teacher";
    }

    @GetMapping("/{practiceId}/create")
    public String createTeam(Model model,
                             @PathVariable Long practiceId) {
        model.addAttribute("practiceId", practiceId);
        return "teacher/createTeam";
    }

    @PostMapping("/{practiceId}/create")
    public String saveTeam(Model model,
                           @PathVariable Long practiceId,
                           @ModelAttribute Team team
    ) {
        model.addAttribute("practiceId", practiceId);
        teamRepo.save(team);
        return ("redirect:/teacher/team/{practiceId}/notIncludedTeams");
    }

    @PostMapping("/assign")
    public String assignTeams(@RequestParam Long practiceId,
                              @RequestParam Long teamId,
                              Model model
    ) {
        teamRepo.assignPracticeToTeam(practiceId, teamId);
        System.out.println(practiceId + " *-* " + teamId);
        model.addAttribute("practiceId", practiceId);
        return ("redirect:/teacher/team/"+practiceId+"/notIncludedTeams");
    }
}
