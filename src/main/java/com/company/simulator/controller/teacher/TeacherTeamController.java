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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/teacher")
public class TeacherTeamController {

    @Autowired
    TeamRepo teamRepo;

    @Autowired
    PracticeRepo practiceRepo;

    @GetMapping("/team")
    public String teamsByAuthor(Model model,
                                @AuthenticationPrincipal User user) {
        final List<Team> teamsInPractice = teamRepo.findTeamsByAuthorId(user.getId()).orElseGet(ArrayList::new);
        model.addAttribute("teams", teamsInPractice);
        return "teacher/team";
    }

    @GetMapping("/team/{practice}")
    public String teamsByPractice(Model model,
                                  @PathVariable Practice practice,
                                  @AuthenticationPrincipal User user) {
        final List<Team> teamsInPractice = teamRepo.findTeamsByPracticesContains(practice).orElseGet(ArrayList::new);
        final List<Team> allAnotherTeamsByAuthor = teamRepo.findTeamsByPracticesNotContainsAndAuthorId(practice, user.getId()).orElseGet(ArrayList::new);
        model.addAttribute("teamsInPractice", teamsInPractice);
        model.addAttribute("allAnotherTeamsByAuthor", allAnotherTeamsByAuthor);
        model.addAttribute("practiceId", practice.getId());
        return "teacher/teamsList";
    }

    @GetMapping("/team/create")
    public String createTeam(Model model) {
        String inviteCode;
        do {
            inviteCode = UUID.randomUUID().toString();
        } while (teamRepo.findTeamByInvitation(inviteCode).isPresent());
        model.addAttribute("inviteCode", inviteCode);
        return "teacher/createTeam";
    }

    @PostMapping("/team/create")
    public String saveTeam(@ModelAttribute Team team) {
        teamRepo.save(team);
        return "redirect:/teacher/team";
    }

    @PostMapping("/team/assign")
    public String assignTeam(@RequestParam Long practiceId,
                             @RequestParam Long teamId
    ) {
        teamRepo.assignPracticeToTeam(practiceId, teamId);
        return String.format("redirect:/teacher/team/%d", practiceId);
    }

    @PostMapping("/team/remove")
    public String removePracticeFromTeam(@RequestParam Long practiceId,
                                         @RequestParam Long teamId
    ) {
        teamRepo.throwPracticeToTeam(practiceId, teamId);
        return String.format("redirect:/teacher/team/%d", practiceId);
    }

    @PostMapping("/team/{practiceId}/start")
    public String startPractice(@PathVariable Long practiceId,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam Boolean sendingAfterDeadLine) {
        practiceRepo.addDeadLineToPractice(practiceId, LocalDateTime.now(), LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)), sendingAfterDeadLine);
        return "redirect:/teacher/practice";
    }
}
