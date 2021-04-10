package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Result;
import com.company.simulator.model.Student;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@RequestMapping("/teacher/statistic")
public class TeacherStatisticController {

    @Autowired
    private SubmissionRepo submissionRepo;

    @GetMapping("/{practice}")
    public String getStatistic(@AuthenticationPrincipal User user,
                               Model model,
                               @PathVariable Practice practice,
                               RedirectAttributes redirectAttributes
    ) {
        try {
            if (user.equals(practice.getAuthor())) {
                final Set<Task> tasks = practice.getTasks();
                final Set<Team> teams = practice.getTeams();
                model.addAttribute("tasks", tasks);
                model.addAttribute("teams", teams);
                model.addAttribute("practice", practice);
                return "teacher/statistic";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such practice");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        }
    }

    @GetMapping("/{practice}/task/{task}")
    public String getStatisticByTask(@PathVariable Practice practice,
                                     @PathVariable Task task,
                                     @AuthenticationPrincipal User user,
                                     Model model,
                                     RedirectAttributes redirectAttributes
    ) {
        try {
            if (user.equals(practice.getAuthor()) && user.equals(task.getAuthor())) {
                final List<Submission> submissions = submissionRepo.findByPracticeAndTask(practice, task).orElseGet(ArrayList::new);
                model.addAttribute("submissions", submissions);
                return "teacher/statisticByTask";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such practice or task");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        }
    }

    @GetMapping("/{practice}/team/{team}/student")
    public String getStudentByTeam(@PathVariable Practice practice,
                                   @PathVariable Team team,
                                   @AuthenticationPrincipal User user,
                                   Model model,
                                   RedirectAttributes redirectAttributes

    ) {
        try {
            if (user.equals(practice.getAuthor()) && user.equals(team.getAuthor())) {
                final Set<Student> students = team.getStudents();
                List<Result> resultsList = new ArrayList<>();
                int totalSum = 0;
                for(Task task: practice.getTasks()) {
                    totalSum += task.getPoints();
                }
                students.forEach(student -> resultsList.add(new Result(student, submissionRepo.findPointsByPracticeAndUser(practice.getId(), student.getId().getUserId()))));
                model.addAttribute("resultsList", resultsList);
                model.addAttribute("totalSum", totalSum);
                return "teacher/studentsByTeamStatistic";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such practice or team");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        }
    }

    @GetMapping("/{practice}/team/{team}/student/{student}")
    public String getStudentsByTeam(@PathVariable Practice practice,
                                    @PathVariable Team team,
                                    @PathVariable User student,
                                    @AuthenticationPrincipal User user,
                                    Model model,
                                    RedirectAttributes redirectAttributes
    ) {
        try {
            if (user.equals(practice.getAuthor()) && user.equals(team.getAuthor())) {
                final List<Submission> submissions = submissionRepo.findByUserAndPractice(student, practice).orElseGet(ArrayList::new);
                model.addAttribute("submissions", submissions);
                return "teacher/statisticByStudent";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such practice, team or student");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        }
    }

    @GetMapping("/{practice}/team/{team}/student/{student}/submission/{submission}")
    public String getSubmissionByStudent(@PathVariable Practice practice,
                                         @PathVariable Team team,
                                         @PathVariable User student,
                                         @PathVariable Submission submission,
                                         @AuthenticationPrincipal User user,
                                         Model model,
                                         RedirectAttributes redirectAttributes
    ) {
        try {
            if (user.equals(practice.getAuthor()) && user.equals(team.getAuthor())) {
                int countOfLength = submission.getQuery().split("\n").length;
                model.addAttribute("countOfLength", countOfLength);
                model.addAttribute("submission", submission);
                return "teacher/submission";
            }
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        } catch (NullPointerException ex) {
            redirectAttributes.addAttribute("message", "There is no such practice, team, student or submission");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice";
        }
    }
}
