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
import java.util.Optional;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private PracticeRepo practiceRepo;

    @GetMapping
    public String teacherPanel(@AuthenticationPrincipal User user,
                               Model model) {
        final Optional<List<Practice>> practices = practiceRepo.findAllPracticeNotInProcess(user.getId());
        if (practices.isPresent()) {
            model.addAttribute("practices", practices.get());
        } else {
            model.addAttribute("practices", new ArrayList<Practice>());
        }

        final Optional<List<Practice>> practicesInProcess = practiceRepo.findAllPracticeInProcess(user.getId(), LocalDateTime.now());
        if (practicesInProcess.isPresent()) {
            model.addAttribute("practicesInProcess", practicesInProcess.get());
        } else {
            model.addAttribute("practicesInProcess", new ArrayList<Practice>());
        }
        return "teacher/panel";
    }

    @GetMapping("archive")
    public String getArchive(@AuthenticationPrincipal User user,
                               Model model) {
        final Optional<List<Practice>> practices = practiceRepo.findAllPracticeInArchive(user.getId(), LocalDateTime.now());
        if (practices.isPresent()) {
            model.addAttribute("practices", practices.get());
        } else {
            model.addAttribute("practices", new ArrayList<Practice>());
        }
        return "teacher/archive";
    }
}
