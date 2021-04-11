package com.company.simulator.controller.result;

import com.company.simulator.exception.AccessDeniedException;
import com.company.simulator.exception.NotFoundException;
import com.company.simulator.model.Submission;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/result/submission")
public class ResultSubmissionController {
    @Autowired
    private SubmissionRepo submRepo;

    @GetMapping
    public String allSubmissions(
        @AuthenticationPrincipal User user,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        Model model
    ) {
        final List<Submission> subms = submRepo.findByUser(user).orElseGet(ArrayList::new);
        model.addAttribute("submissions", subms);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "result/resultSubmission";
    }

    @GetMapping("{subm}")
    public String submissionById(
        @AuthenticationPrincipal User user,
        @PathVariable Submission subm,
        Model model
    ) {
        if (subm == null) {
            throw new NotFoundException("There is no such submission");
        }
        if (!subm.getUser().equals(user)) {
            throw new AccessDeniedException(
                String.format("Access to submission `%d` denied", subm.getId())
            );
        }
        model.addAttribute("submission", subm);
        return "result/submissionInfo";
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
            String.format("attachment; filename=submissions_%s.csv", currDate)
        );
        final List<Submission> sbmns = submRepo.findByUser(user).orElseGet(ArrayList::new);
        final String[] csvHeader = {
            "ID", "Query", "Is_correct", "Send_date", "Practice_ID",
            "Practice_name", "Task_ID", "Task_name", "User_id", "Username"
        };
        try (CSVPrinter csvPrinter = new CSVPrinter(
            response.getWriter(),
            CSVFormat.DEFAULT.withHeader(csvHeader)
        )) {
            for (Submission subm : sbmns) {
                csvPrinter.printRecord(
                    Arrays.asList(
                        subm.getId(),
                        subm.getQuery(),
                        subm.isCorrect(),
                        subm.getSendDate(),
                        subm.getPractice().getId(),
                        subm.getPractice().getName(),
                        subm.getTask().getId(),
                        subm.getTask().getName(),
                        subm.getUser().getId(),
                        subm.getUser().getUsername()
                    )
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }
}
