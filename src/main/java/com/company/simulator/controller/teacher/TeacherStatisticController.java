package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Student;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.SubmissionRepo;
import com.company.simulator.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/teacher/statistic")
public class TeacherStatisticController {

    @Autowired
    private SubmissionRepo submissionRepo;

    @Autowired
    private PracticeRepo practiceRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("/{practice}")
    public String getStatistic(@AuthenticationPrincipal User user,
                               Model model,
                               @PathVariable Practice practice) {
        final Set<Task> tasks = practice.getTasks();
        final Set<Team> teams = practice.getTeams();
        final List<Submission> submissions = submissionRepo.findByPractice(practice).orElseGet(ArrayList::new);
        model.addAttribute("tasks", tasks);
        model.addAttribute("teams", teams);
        model.addAttribute("practice", practice);
        return "teacher/statistic";
    }

    @GetMapping("/{practice}/task/{task}")
    public String getStatisticByTask(@PathVariable Practice practice,
                                     @PathVariable Task task,
                                     @AuthenticationPrincipal User user,
                                     Model model
    ) {
        final List<Submission> submissions = submissionRepo.findByPracticeAndTask(practice, task).orElseGet(ArrayList::new);
        model.addAttribute("submissions", submissions);
        return "teacher/statisticByTask";
    }

    @GetMapping("/{practice}/team/{team}/student")
    public String getStudentsByTeam(@PathVariable Practice practice,
                                    @PathVariable Team team,
                                    @AuthenticationPrincipal User user,
                                    Model model
    ) {
        final Set<Student> students = team.getStudents();
        model.addAttribute("students", students);
        return "teacher/studentsByTeamStatistic";
    }

    @GetMapping("/{practice}/team/{team}/student/{student}")
    public String getStudentsByTeam(@PathVariable Practice practice,
                                    @PathVariable Team team,
                                    @PathVariable User student,
                                    @AuthenticationPrincipal User user,
                                    Model model) {
        final List<Submission> submissions = submissionRepo.findByUserAndPractice(student, practice).orElseGet(ArrayList::new);
        model.addAttribute("submissions", submissions);
        return "teacher/statisticByStudent";
    }

    @GetMapping("/{practice}/team/{team}/student/{student}/submission/{submission}")
    public String getSubmissionByStudent(@PathVariable Practice practice,
                                         @PathVariable Team team,
                                         @PathVariable User student,
                                         @PathVariable Submission submission,
                                         @AuthenticationPrincipal User user,
                                         Model model) {
        int countOfLength = submission.getQuery().split("\n").length;
        model.addAttribute("countOfLength", countOfLength);
        model.addAttribute("submission", submission);
        return "teacher/submission";
    }
}
