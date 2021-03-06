package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.repos.PracticeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private PracticeRepo practiceRepo;

    @GetMapping
    public String teacherPanel(Model model) {
        final Iterable<Practice> practices = practiceRepo.findAll();
        model.addAttribute("practices", practices);
        return "teacher/panel";
    }
}
