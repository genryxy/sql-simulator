package com.company.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResultController {
    @GetMapping("/result")
    public String results(Model model) {
        return "result/result";
    }

    @GetMapping("/result/practice")
    public String resultsByPractice(Model model) {
        return "result/result";
    }

    @GetMapping("/result/task")
    public String resultsByTask(Model model) {
        return "result/result";
    }
}
