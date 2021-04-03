package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.StudentRepo;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    StudentRepo studentRepo;

    @Autowired
    PracticeRepo practiceRepo;

    @GetMapping("/team")
    public String teamsByAuthor(Model model,
                                @AuthenticationPrincipal User user) {
        final List<Team> teamsInPractice = teamRepo.findTeamsByAuthorId(user.getId()).orElseGet(ArrayList::new);
        model.addAttribute("teams", teamsInPractice);
        return "teacher/team";
    }

    @GetMapping("team/{team}/info")
    public String getTeam(@PathVariable("team") Team team,
                          @RequestParam(required = false) String message,
                          @RequestParam(required = false) String type,
                          Model model) {
        model.addAttribute("team", team);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/teamInfo";
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
    public String removeTeamFromPractice(@RequestParam Long practiceId,
                                         @RequestParam Long teamId
    ) {
        teamRepo.throwPracticeToTeam(practiceId, teamId);
        return String.format("redirect:/teacher/team/%d", practiceId);
    }

    @PostMapping("/team/{practiceId}/start")
    public String startPractice(@PathVariable Long practiceId,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam(required = false) Boolean sendingAfterDeadLine) {
        practiceRepo.addDeadLineToPractice(practiceId, LocalDateTime.now(), LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)), sendingAfterDeadLine != null);
        return "redirect:/teacher/practice";
    }

    @GetMapping("team/{team}/edit")
    public String editTeam(
        @PathVariable Team team,
        Model model,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type
    ) {
        model.addAttribute("task", team);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/teamEdit";
    }

    @PostMapping("team/{team}/edit")
    public String saveEditTask(
        @PathVariable Team team,
        @ModelAttribute Team editedTeam,
        RedirectAttributes redirectAttributes
    ) {
        try {
            teamRepo.updateTeam(team.getId(),
                                editedTeam.getName());
            redirectAttributes.addAttribute("message", "Team successfully edited");
            redirectAttributes.addAttribute("type", "success");
            return String.format("redirect:/teacher/team/%d/info", team.getId());
        } catch (NullPointerException e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("type", "danger");
            return String.format("redirect:/teacher/team/%d/edit", team.getId());
        }
    }

    @PostMapping("team/{team}/remove")
    public String removeTeam(
        @PathVariable Team team
    ) {
        teamRepo.delete(team);
        return "redirect:/teacher/team";
    }

    @PostMapping("team/{team}/remove/{user}")
    public String removeUserFromTeam(
        @PathVariable User user,
        @PathVariable Team team) {
        studentRepo.deleteByUserAndTeam(user, team);
        return String.format("redirect:/teacher/team/%d/info", team.getId());
    }
}
