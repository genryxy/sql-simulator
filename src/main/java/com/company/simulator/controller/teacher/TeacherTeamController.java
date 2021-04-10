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
                                @RequestParam(required = false) String message,
                                @RequestParam(required = false) String type,
                                @AuthenticationPrincipal User user) {
        final List<Team> teamsInPractice = teamRepo.findTeamsByAuthorId(user.getId()).orElseGet(ArrayList::new);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        model.addAttribute("teams", teamsInPractice);
        return "teacher/team";
    }

    @GetMapping("team/{team}/info")
    public String getTeam(@PathVariable("team") Team team,
                          @AuthenticationPrincipal User user,
                          @RequestParam(required = false) String message,
                          @RequestParam(required = false) String type,
                          RedirectAttributes redirectAttributes,
                          Model model
    ) {
        try {
            if (user.equals(team.getAuthor())) {
                model.addAttribute("team", team);
                model.addAttribute("message", message);
                model.addAttribute("type", type);
                return "teacher/teamInfo";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/team";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such team");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/team";
        }
    }

    @GetMapping("/team/{practice}")
    public String teamsByPractice(Model model,
                                  @PathVariable Practice practice,
                                  @AuthenticationPrincipal User user,
                                  @RequestParam(required = false) String message,
                                  @RequestParam(required = false) String type,
                                  RedirectAttributes redirectAttributes
    ) {
        try {
            model.addAttribute("message", message);
            model.addAttribute("type", type);
            if (user.equals(practice.getAuthor())) {
                final List<Team> teamsInPractice = teamRepo.findTeamsByPracticesContains(practice).orElseGet(ArrayList::new);
                final List<Team> allAnotherTeamsByAuthor = teamRepo.findTeamsByPracticesNotContainsAndAuthorId(practice, user.getId()).orElseGet(ArrayList::new);
                model.addAttribute("teamsInPractice", teamsInPractice);
                model.addAttribute("allAnotherTeamsByAuthor", allAnotherTeamsByAuthor);
                model.addAttribute("practice", practice);
                return "teacher/teamsList";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such practice");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/team";
        }
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
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes
    ) {
        if (user.equals(practice.getAuthor())
            && user.equals(team.getAuthor())) {
            teamRepo.assignPracticeToTeam(practice.getId(), team.getId());
            return String.format("redirect:/teacher/team/%d", practice.getId());
        }
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
        return "redirect:/teacher/practice";
    }

    @PostMapping("/team/remove")
    public String removeTeamFromPractice(@RequestParam Practice practice,
                                         @RequestParam Team team,
                                         @AuthenticationPrincipal User user,
                                         RedirectAttributes redirectAttributes
    ) {
        if (user.equals(practice.getAuthor())
            && user.equals(team.getAuthor())) {
            teamRepo.throwPracticeToTeam(practice.getId(), team.getId());
            return String.format("redirect:/teacher/team/%d", practice.getId());
        }
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
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
        try {
            if (user.equals(practice.getAuthor())) {
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
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return String.format("redirect:/teacher/team/%d", practice.getId());
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such practice");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/team";
        }
    }

    @GetMapping("team/{team}/edit")
    public String editTeam(
        @PathVariable Team team,
        @AuthenticationPrincipal User user,
        Model model,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        RedirectAttributes redirectAttributes
    ) {
        try {
            if (user.equals(team.getAuthor())) {
                model.addAttribute("task", team);
                model.addAttribute("message", message);
                model.addAttribute("type", type);
                return "teacher/teamEdit";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/team";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such team");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/team";
        }

    }

    @PostMapping("team/{team}/edit")
    public String saveEditTask(
        @PathVariable Team team,
        @ModelAttribute Team editedTeam,
        @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes
    ) {
        if (user.equals(team.getAuthor())) {
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
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
        return "redirect:/teacher/team";
    }

    @PostMapping("team/{team}/remove")
    public String removeTeam(
        @PathVariable Team team,
        @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes
    ) {
        if (user.equals(team.getAuthor())) {
            teamRepo.delete(team);
        }
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
        return "redirect:/teacher/team";
    }

    @PostMapping("team/{team}/remove/{student}")
    public String removeUserFromTeam(
        @PathVariable User student,
        @PathVariable Team team,
        @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes) {
        if (user.equals(team.getAuthor())) {
            studentRepo.deleteByUserAndTeam(student, team);
            return String.format("redirect:/teacher/team/%d/info", team.getId());
        }
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
        return "redirect:/teacher/team";
    }
}
