package com.company.simulator.controller.teacher;

import com.company.simulator.model.Task;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.TaskRepo;
import com.company.simulator.transaction.SqlTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teacher/task")
public class TeacherTaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private SqlTransaction sqlTransaction;

    @GetMapping
    public String task(Model model) {
        return "task";
    }

    // TODO use this method to check dsl from teacher
    @GetMapping("/createTable")
    public ResponseEntity executeQuery() {
        String example ="create table test_table (\n" +
                "    id      int8 not null,\n" +
                "    tag     varchar(255),\n" +
                "    text    varchar(65535) not null,\n" +
                "    user_id int8,\n" +
                "    primary key (id)\n" +
                ");" +
                "insert into test_table values (1,'123', '1234', 1);" +
                "insert into test_table values (2,'123', '1234', 1);" +
                "insert into test_table values (3,'123', '1234', 1);" +
                "create table task2 (\n" +
                "    id      int8 not null,\n" +
                "    tag     varchar(255),\n" +
                "    text    varchar(65535) not null,\n" +
                "    user_id int8,\n" +
                "    primary key (id)\n" +
                ");";
        return sqlTransaction.executeQuery(example, "select * from test_table", "select * from test_table");
    }

    @GetMapping("/all")
    public String taskList(Model model) {
        final List<Task> tasks = (List<Task>) taskRepo.findAll();
        model.addAttribute("tasks", tasks);
        return "practice/taskList";
    }

    @GetMapping("/{task}")
    public String getTask(@PathVariable("task") Task task, Model model) {
        model.addAttribute("task", task);
        return "teacher/taskInfo";
    }

    @GetMapping("/create")
    public String createTask(Model model,
                             @RequestParam(required = false) String message,
                             @RequestParam(required = false) String type
    ) {
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/createTask";
    }

    @PostMapping("/create")
    public String addTask(@ModelAttribute Task task,
                          RedirectAttributes redirectAttributes
    ) {
        String message = "Successfully created",
                type = "success";
        try{
            sqlTransaction.validationTeacherQuery(task.getDdlScript(), task.getCorrectQuery());
            taskRepo.save(task);
        } catch (Exception e){
            message = e.getMessage();
            type = "danger";
        }

        redirectAttributes.addAttribute("message", message);
        redirectAttributes.addAttribute("type", type);
        return ("redirect:/teacher/task/create");
    }
}
