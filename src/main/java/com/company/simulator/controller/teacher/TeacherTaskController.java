package com.company.simulator.controller.teacher;

import com.company.simulator.model.Task;
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

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherTaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private SqlTransaction sqlTransaction;

    @GetMapping("")
    public String teacherPanel(Model model) {
        return "teacher/panel";
    }

    @GetMapping("/task")
    public String task(Model model) {
        return "task";
    }

    // TODO use this method in execute task by student
    @GetMapping("/task/createTable")
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

    @GetMapping("/task/all")
    public String taskList(Model model) {
        final List<Task> tasks = (List<Task>) taskRepo.findAll();
        model.addAttribute("tasks", tasks);
        return "practice/taskList";
    }

    @GetMapping(value = "/task/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTask(@PathVariable("id") Task task) {
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/task/create")
    public String createTask(Model model) {
        return "createTask";
    }

    @PostMapping("/task/create")
    public String addTask(@ModelAttribute Task task) {
        taskRepo.save(task);
        return ("task");
    }
}
