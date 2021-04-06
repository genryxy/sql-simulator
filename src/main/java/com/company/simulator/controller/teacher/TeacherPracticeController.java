package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/teacher/practice")
public class TeacherPracticeController {
    @Autowired
    private PracticeRepo practiceRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping
    public String getPractices(@AuthenticationPrincipal User user,
                               Model model) {
        final List<Practice> practices = practiceRepo.findAllPracticeNotInProcess(user.getId()).orElseGet(ArrayList::new);
        model.addAttribute("practices", practices);

        final List<Practice> practicesInProcess = practiceRepo.findAllPracticeInProcess(user.getId(), LocalDateTime.now()).orElseGet(ArrayList::new);
        model.addAttribute("practicesInProcess", practicesInProcess);
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
        Model model
    ) {
        final List<Task> tasks = new ArrayList<>(practice.getTasks());
        LocalDateTime deadLine = practiceRepo.getDeadlineByPracticeId(practice.getId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("practice", practice);
        model.addAttribute("deadLine", deadLine);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/practiceInfo";
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
            redirectAttributes.addAttribute("message", "Practice successfully edited");
            redirectAttributes.addAttribute("type", "success");
            return ("redirect:/teacher/practice");
        } catch (NullPointerException e){
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
        @PathVariable Practice practice
    ) {
        practiceRepo.delete(practice);
        return "redirect:/teacher/practice";
    }

    @GetMapping("{practice}/edit")
    public String editPractice(
        @PathVariable Practice practice,
        Model model,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String type
    ) {
        LocalDateTime deadline = practiceRepo.getDeadlineByPracticeId(practice.getId());
        model.addAttribute("practice", practice);
        model.addAttribute("deadLine", deadline);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/practiceEdit";
    }

    @PostMapping("{practice}/edit")
    public String saveEditTask(
        @PathVariable Practice practice,
        @ModelAttribute Practice editedPractice,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String time,
        @RequestParam(required = false) Boolean sendingAfterDeadLine,
        RedirectAttributes redirectAttributes
    ) {
        try {
            practiceRepo.updatePractice(practice.getId(), editedPractice.getName(), editedPractice.getDescription());
            if (practiceRepo.getDeadlineByPracticeId(practice.getId()) != null) {
                practiceRepo.updateDeadLineToPractice(practice.getId(), LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)), sendingAfterDeadLine != null);
            }
            redirectAttributes.addAttribute("message", "Practice successfully edited");
            redirectAttributes.addAttribute("type", "success");
            return String.format("redirect:/teacher/practice/%d/info", practice.getId());
        } catch (NullPointerException e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("type", "danger");
            return String.format("redirect:/teacher/practice/%d/edit", practice.getId());
        }
    }
}
