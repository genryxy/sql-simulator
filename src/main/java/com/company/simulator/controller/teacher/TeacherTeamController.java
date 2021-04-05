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
                          @AuthenticationPrincipal User user,
                          @RequestParam(required = false) String message,
                          @RequestParam(required = false) String type,
                          Model model) {
        if (user.getId().equals(team.getAuthor().getId())) {
            model.addAttribute("team", team);
            model.addAttribute("message", message);
            model.addAttribute("type", type);
            return "teacher/teamInfo";
        }
        return "redirect:/teacher/team";
    }

    @GetMapping("/team/{practice}")
    public String teamsByPractice(Model model,
                                  @PathVariable Practice practice,
                                  @AuthenticationPrincipal User user,
                                  @RequestParam(required = false) String message,
                                  @RequestParam(required = false) String type
    ) {
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        if (user.getId().equals(practice.getAuthorId())) {
            final List<Team> teamsInPractice = teamRepo.findTeamsByPracticesContains(practice).orElseGet(ArrayList::new);
            final List<Team> allAnotherTeamsByAuthor = teamRepo.findTeamsByPracticesNotContainsAndAuthorId(practice, user.getId()).orElseGet(ArrayList::new);
            model.addAttribute("teamsInPractice", teamsInPractice);
            model.addAttribute("allAnotherTeamsByAuthor", allAnotherTeamsByAuthor);
            model.addAttribute("practice", practice);
            return "teacher/teamsList";
        }
        return "redirect:/teacher/practice";

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
    public String assignTeam(@RequestParam Practice practice,
                             @RequestParam Team team,
                             @AuthenticationPrincipal User user
    ) {
        if (user.getId().equals(practice.getAuthorId())
            && user.getId().equals(team.getAuthor().getId())) {
            teamRepo.assignPracticeToTeam(practice.getId(), team.getId());
            return String.format("redirect:/teacher/team/%d", practice.getId());
        }
        return "redirect:/teacher/practice";
    }

    @PostMapping("/team/remove")
    public String removeTeamFromPractice(@RequestParam Practice practice,
                                         @RequestParam Team team,
                                         @AuthenticationPrincipal User user
    ) {
        if (user.getId().equals(practice.getAuthorId())
            && user.getId().equals(team.getAuthor().getId())) {
            teamRepo.throwPracticeToTeam(practice.getId(), team.getId());
            return String.format("redirect:/teacher/team/%d", practice.getId());
        }
        return "redirect:/teacher/practice";
    }

    @PostMapping("/team/{practice}/start")
    public String startPractice(@PathVariable Practice practice,
                                @AuthenticationPrincipal User user,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam(required = false) Boolean sendingAfterDeadLine,
                                RedirectAttributes redirectAttributes
    ) {
        if (user.getId().equals(practice.getAuthorId())) {
            LocalDateTime newTimestamp = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
            if (newTimestamp.isBefore(LocalDateTime.now())) {
                redirectAttributes.addAttribute("message", "Incorrect Deadline");
                redirectAttributes.addAttribute("type", "danger");
                return "redirect:/teacher/practice";
            }
            redirectAttributes.addAttribute("message", "Practice successfully assigned");
            redirectAttributes.addAttribute("type", "success");
            practiceRepo.addDeadLineToPractice(practice.getId(), LocalDateTime.now(), newTimestamp, sendingAfterDeadLine != null);
            return "redirect:/teacher/practice";
        }
        return String.format("redirect:/teacher/team/%d", practice.getId());
    }

    @GetMapping("team/{team}/edit")
    public String editTeam(
        @PathVariable Team team,
        @AuthenticationPrincipal User user,
        Model model,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type
    ) {
        if (user.getId().equals(team.getAuthor().getId())) {
            model.addAttribute("task", team);
            model.addAttribute("message", message);
            model.addAttribute("type", type);
            return "teacher/teamEdit";
        }
        return "redirect:/teacher/team";
    }

    @PostMapping("team/{team}/edit")
    public String saveEditTask(
        @PathVariable Team team,
        @ModelAttribute Team editedTeam,
        @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes
    ) {
        if (user.getId().equals(team.getAuthor().getId())) {
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
        return "redirect:/teacher/team";
    }

    @PostMapping("team/{team}/remove")
    public String removeTeam(
        @PathVariable Team team,
        @AuthenticationPrincipal User user
    ) {
        if (user.getId().equals(team.getAuthor().getId())) {
            teamRepo.delete(team);
        }
        return "redirect:/teacher/team";
    }

    @PostMapping("team/{team}/remove/{student}")
    public String removeUserFromTeam(
        @PathVariable User student,
        @PathVariable Team team,
        @AuthenticationPrincipal User user) {
        if (user.getId().equals(team.getAuthor().getId())) {
            studentRepo.deleteByUserAndTeam(student, team);
            return String.format("redirect:/teacher/team/%d/info", team.getId());
        }
        return "redirect:/teacher/team";
    }
}
