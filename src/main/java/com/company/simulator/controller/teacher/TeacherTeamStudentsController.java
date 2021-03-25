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
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/team")
public class TeacherTeamStudentsController {

    @Autowired
    TeamRepo teamRepo;

    @Autowired
    PracticeRepo practiceRepo;

    @GetMapping("/{practice}")
    public String teamsByPractice(Model model,
                                  @PathVariable Practice practice,
                                  @AuthenticationPrincipal User user) {

        final Optional<List<Team>> teams = teamRepo.findTeamsByPracticesContains(practice);
        if (teams.isPresent()) {
            model.addAttribute("teams", teams.get());
        } else {
            model.addAttribute("teams", new ArrayList<Team>());
        }

        final Optional<List<Team>> teams2 = teamRepo.findTeamsByPracticesNotContainsAndAuthorId(practice, user.getId());
        if (teams2.isPresent()) {
            model.addAttribute("teams2", teams2.get());
        } else {
            model.addAttribute("teams2", new ArrayList<Team>());
        }

        model.addAttribute("practiceId", practice.getId());
        return "teacher/teamsList";
    }

    @GetMapping("/{practiceId}/create")
    public String createTeam(Model model,
                             @PathVariable Long practiceId) {
        String inviteCode;
        do {
            inviteCode = new Random()
                    .ints(10, 33, 122)
                    .mapToObj(i -> String.valueOf((char) i))
                    .collect(Collectors.joining());
        } while (teamRepo.findTeamByInvitation(inviteCode).isPresent());
        model.addAttribute("inviteCode", inviteCode);
        model.addAttribute("practiceId", practiceId);
        return "teacher/createTeam";
    }

    @PostMapping("/{practiceId}/create")
    public String saveTeam(@PathVariable Long practiceId,
                           @ModelAttribute Team team
    ) {
        teamRepo.save(team);
        return String.format("redirect:/teacher/team/%d", practiceId);
    }

    @PostMapping("/assign")
    public String assignTeam(@RequestParam Long practiceId,
                             @RequestParam Long teamId
    ) {
        teamRepo.assignPracticeToTeam(practiceId, teamId);
        return String.format("redirect:/teacher/team/%d", practiceId);
    }

    @PostMapping("/throw")
    public String throwTeam(@RequestParam Long practiceId,
                            @RequestParam Long teamId
    ) {
        teamRepo.throwPracticeToTeam(practiceId, teamId);
        return String.format("redirect:/teacher/team/%d", practiceId);
    }

    @PostMapping("{practiceId}/start")
    public String startPractice(@PathVariable Long practiceId,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam Boolean checkBox) {
        practiceRepo.addDeadLineToPractice(practiceId, LocalDateTime.now(), LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)), checkBox);
        return "redirect:/teacher";
    }
}
