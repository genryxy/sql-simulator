package com.company.simulator.controller.result;

import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.ResultTask;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import com.company.simulator.repos.TaskRepo;
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
@RequestMapping("/result/task")
public class ResultTaskController {
    @Autowired
    private SubmissionRepo submRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping
    public String getAllAvailableTasks(
        @AuthenticationPrincipal User user,
        Model model
    ) {
        final List<ResultTask> resTasks = resultsOfTasksFor(user);
        model.addAttribute("results", resTasks);
        return "result/resultTask";
    }

    @GetMapping("{task}")
    public String submissionsByTask(
        @AuthenticationPrincipal User user,
        @PathVariable Task task,
        Model model
    ) {
        if (task == null) {
            throw new NotFoundException("There is no such task");
        }
        model.addAttribute(
            "submissions",
            submRepo.findByUserAndTask(user, task).orElseGet(ArrayList::new)
        );
        return "result/oneTask";
    }

    @GetMapping("export")
    public void exportResultsForSubmissions(
        @AuthenticationPrincipal User user,
        HttpServletResponse response
    ) {
        response.setContentType("text/csv");
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        final String currDate = dateFormatter.format(new Date());
        response.setHeader(
            "Content-Disposition",
            String.format("attachment; filename=tasks_%s.csv", currDate)
        );
        final List<ResultTask> sbmns = resultsOfTasksFor(user);
        final String[] csvHeader = {
            "Task_ID", "Task_name", "Points", "Category",
            "Number_correct_attempts", "Total_attempts"
        };
        try (CSVPrinter csvPrinter = new CSVPrinter(
            response.getWriter(),
            CSVFormat.DEFAULT.withHeader(csvHeader)
        )) {
            for (ResultTask res : sbmns) {
                csvPrinter.printRecord(
                    Arrays.asList(
                        res.getTask().getId(),
                        res.getTask().getName(),
                        res.getTask().getPoints(),
                        res.getTask().getCategory().getName(),
                        res.getCorrect(),
                        res.getTotal()
                    )
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    private List<ResultTask> resultsOfTasksFor(User user) {
        return taskRepo.findAllForUser(user.getId())
            .orElseGet(ArrayList::new).stream()
            .map(
                task -> {
                    final Map<String, Number> res;
                    res = submRepo.findNumberAttemptsForTaskAndUser(task.getId(), user.getId());
                    return new ResultTask(
                        task,
                        res.get("total").intValue(),
                        res.get("correct").intValue()
                    );
                }
            ).collect(Collectors.toList());
    }
}
