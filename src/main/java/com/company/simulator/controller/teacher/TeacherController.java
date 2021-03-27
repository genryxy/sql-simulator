package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private PracticeRepo practiceRepo;

    @GetMapping
    public String teacherPanel(@AuthenticationPrincipal User user,
                               Model model) {
        final List<Practice> practices = practiceRepo.findAllPracticeNotInProcess(user.getId()).orElseGet(ArrayList::new);
        model.addAttribute("practices", practices);

        final List<Practice> practicesInProcess = practiceRepo.findAllPracticeInProcess(user.getId(), LocalDateTime.now()).orElseGet(ArrayList::new);
        model.addAttribute("practicesInProcess", practicesInProcess);
        return "teacher/panel";
    }

    @GetMapping("archive")
    public String getArchivedPractices(@AuthenticationPrincipal User user,
                               Model model) {
        final List<Practice> practices = practiceRepo.findAllPracticeInArchive(user.getId(), LocalDateTime.now()).orElseGet(ArrayList::new);
        model.addAttribute("practices", practices);
        return "teacher/archive";
    }
}
