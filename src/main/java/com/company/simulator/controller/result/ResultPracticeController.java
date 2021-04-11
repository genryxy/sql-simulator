package com.company.simulator.controller.result;

import com.company.simulator.access.AccessStudent;
import com.company.simulator.exception.AccessDeniedException;
import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.Practice;
import com.company.simulator.model.ResultPractice;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.processing.TasksMarked;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.StudentRepo;
import com.company.simulator.repos.SubmissionRepo;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/result/practice")
public class ResultPracticeController {
    @Autowired
    private SubmissionRepo submRepo;

    @Autowired
    private PracticeRepo practiceRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TasksMarked tasksMarked;

    @GetMapping
    public String resultsForPractices(
        @AuthenticationPrincipal User user,
        Model model
    ) {
        final List<ResultPractice> resPracs = resultsOfPracticesFor(user);
        model.addAttribute("results", resPracs);
        return "result/resultPractice";
    }

    @GetMapping("export")
    public void exportResultsForPractices(
        @AuthenticationPrincipal User user,
        HttpServletResponse response
    ) {
        response.setContentType("text/csv");
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        final String currDate = dateFormatter.format(new Date());
        response.setHeader(
            "Content-Disposition",
            String.format("attachment; filename=practices_%s.csv", currDate)
        );
        final List<ResultPractice> resPracs = resultsOfPracticesFor(user);
        final String[] csvHeader = {"Practice_ID", "Practice_name", "Score", "Total"};
        try (CSVPrinter csvPrinter = new CSVPrinter(
            response.getWriter(),
            CSVFormat.DEFAULT.withHeader(csvHeader)
        )) {
            for (ResultPractice res : resPracs) {
                csvPrinter.printRecord(
                    Arrays.asList(
                        res.getPractice().getId(),
                        res.getPractice().getName(),
                        res.getScore(),
                        res.getTotal()
                    )
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    @GetMapping("{practice}")
    public String resultsByPractice(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        Model model
    ) {
        if (practice == null) {
            throw new NotFoundException("There is no such practice");
        }
        if (!new AccessStudent(user, studentRepo).toPractice(practice)) {
            throw new AccessDeniedException(
                String.format(
                    "Access to practice id `%d` is denied",
                    practice.getId()
                )
            );
        }
        final List<Task> tasks = tasksMarked.markedStatus(practice.getTasks(), practice, user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("practice", practice);
        return "result/onePractice";
    }

    @GetMapping("{practice}/task/{task}")
    public String resultsByPracticeAndTask(
        @AuthenticationPrincipal User user,
        @PathVariable Practice practice,
        @PathVariable Task task,
        Model model
    ) {
        if (practice == null) {
            throw new NotFoundException("There is no such practice");
        }
        if (task == null) {
            throw new NotFoundException("There is no such task");
        }
        if (!new AccessStudent(user, studentRepo).toPractice(practice)) {
            throw new AccessDeniedException(
                String.format(
                    "Access to practice id `%d` is denied",
                    practice.getId()
                )
            );
        }
        model.addAttribute(
            "submissions",
            submRepo.findByUserAndPracticeAndTask(user, practice, task).orElseGet(ArrayList::new)
        );
        return "result/oneTaskFromPractice";
    }

    private List<ResultPractice> resultsOfPracticesFor(User user) {
        return practiceRepo.findAllForUserExcept(user.getId(), Practice.COMMON_POOL)
            .orElseGet(ArrayList::new).stream()
            .map(
                prac -> {
                    final Map<String, Number> res;
                    res = submRepo.findScoreToTotalForPracticeByUser(prac.getId(), user.getId());
                    return new ResultPractice(
                        prac,
                        res.get("total").intValue(),
                        res.get("score").intValue()
                    );
                }
            ).collect(Collectors.toList());
    }
}
