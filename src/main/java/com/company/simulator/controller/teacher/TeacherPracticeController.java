package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
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

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@RequestMapping("/teacher/practice")
public class TeacherPracticeController {
    @Autowired
    private PracticeRepo practiceRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping
    public String getPractices(@AuthenticationPrincipal User user,
                               @RequestParam(required = false) String message,
                               @RequestParam(required = false) String type,
                               Model model) {
        final List<Practice> practices = practiceRepo.findAllPracticeNotInProcess(user.getId()).orElseGet(ArrayList::new);
        final List<Practice> practicesInProcess = practiceRepo.findAllPracticeInProcess(user.getId(), LocalDateTime.now()).orElseGet(ArrayList::new);
        model.addAttribute("practices", practices);
        model.addAttribute("practicesInProcess", practicesInProcess);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/practice";
    }

    @GetMapping("archive")
    public String getArchivedPractices(@AuthenticationPrincipal User user,
                                       Model model) {
        final List<Practice> practices = practiceRepo.findAllPracticeInArchive(user.getId(), LocalDateTime.now()).orElseGet(ArrayList::new);
        model.addAttribute("practices", practices);
        return "teacher/archive";
    }

    @GetMapping("/{practice}/info")
    public String getPractice(
        @PathVariable Practice practice,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        RedirectAttributes redirectAttributes,
        @AuthenticationPrincipal User user,
        Model model
    ) {
        try {
            if (user.equals(practice.getAuthor()) && practice.getId() != 1) {
                final List<Task> tasks = new ArrayList<>(practice.getTasks());
                LocalDateTime deadLine = practiceRepo.getDeadlineByPracticeId(practice.getId());
                model.addAttribute("tasks", tasks);
                model.addAttribute("practice", practice);
                model.addAttribute("deadLine", deadLine);
                model.addAttribute("message", message);
                model.addAttribute("type", type);
                return "teacher/practiceInfo";
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

    @GetMapping("/create")
    public String createPractice(@AuthenticationPrincipal User user,
                                 Model model,
                                 @RequestParam(required = false) String message,
                                 @RequestParam(required = false) String type) {
        final Iterable<Task> tasks = taskRepo.findAllTaskByAuthorId(user.getId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/createPractice";
    }

    @PostMapping("/create")
    public String savePractice(@ModelAttribute Practice practice,
                               @RequestParam MultiValueMap<String, String> checkBoxes,
                               RedirectAttributes redirectAttributes
    ) {
        try {
            practiceRepo.save(practice);
            final Iterable<Task> tasks = taskRepo.findAll();
            for (Task task : tasks) {
                Long taskId = task.getId();
                if (checkBoxes.get("checkBox" + taskId) != null) {
                    taskRepo.addTaskToPractice(practice.getId(), taskId);
                }
            }
            redirectAttributes.addAttribute("message", "Practice successfully created");
            redirectAttributes.addAttribute("type", "success");
            return ("redirect:/teacher/practice");
        } catch (NullPointerException e) {
            redirectAttributes.addAttribute("message", "Input all data!");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice/create";
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/teacher/practice/create";
        }
    }

    @PostMapping("/{practice}/remove")
    public String removePractice(
        @PathVariable Practice practice,
        @AuthenticationPrincipal User user,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (user.equals(practice.getAuthor()) && practice.getId() != 1) {
            practiceRepo.delete(practice);
            model.addAttribute("message", "Practice removed");
            model.addAttribute("type", "success");
        } else {
            redirectAttributes.addAttribute("message", "No Access");
            redirectAttributes.addAttribute("type", "danger");
        }
        return "redirect:/teacher/practice";
    }

    @GetMapping("{practice}/edit")
    public String editPractice(
        @PathVariable Practice practice,
        @AuthenticationPrincipal User user,
        Model model,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type,
        RedirectAttributes redirectAttributes
    ) {
        try {
            if (user.equals(practice.getAuthor()) && practice.getId() != 1) {
                LocalDateTime deadline = practiceRepo.getDeadlineByPracticeId(practice.getId());
                model.addAttribute("practice", practice);
                model.addAttribute("deadline", deadline);
                model.addAttribute("message", message);
                model.addAttribute("type", type);
                return "teacher/practiceEdit";
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

    @PostMapping("{practice}/edit")
    public String saveEditTask(
        @PathVariable Practice practice,
        @AuthenticationPrincipal User user,
        @ModelAttribute Practice editedPractice,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String time,
        @RequestParam(required = false) Boolean sendingAfterDeadLine,
        RedirectAttributes redirectAttributes
    ) {
        if (user.equals(practice.getAuthor()) && practice.getId() != 1) {
            try {
                if (practiceRepo.getDeadlineByPracticeId(practice.getId()) != null) {
                    LocalDateTime newTimestamp = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
                    if (newTimestamp.isBefore(LocalDateTime.now())) {
                        redirectAttributes.addAttribute("message", "Incorrect Deadline");
                        redirectAttributes.addAttribute("type", "danger");
                        return String.format("redirect:/teacher/practice/%d/edit", practice.getId());
                    }
                    practiceRepo.updateDeadLineToPractice(practice.getId(), newTimestamp, sendingAfterDeadLine != null);
                }
                practiceRepo.updatePractice(practice.getId(), editedPractice.getName(), editedPractice.getDescription());
                redirectAttributes.addAttribute("message", "Practice successfully edited");
                redirectAttributes.addAttribute("type", "success");
                return String.format("redirect:/teacher/practice/%d/info", practice.getId());
            } catch (NullPointerException e) {
                redirectAttributes.addAttribute("message", e.getMessage());
                redirectAttributes.addAttribute("type", "danger");
                return String.format("redirect:/teacher/practice/%d/edit", practice.getId());
            }
        }
        redirectAttributes.addAttribute("message", "No Access");
        redirectAttributes.addAttribute("type", "danger");
        return "redirect:/teacher/practice";
    }
}
