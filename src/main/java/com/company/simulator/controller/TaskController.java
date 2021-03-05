package com.company.simulator.controller;

import com.company.simulator.transaction.SqlTransaction;
import com.company.simulator.model.StudentQuery;
import com.company.simulator.model.Task;
import com.company.simulator.repos.StudentQueryRepo;
import com.company.simulator.repos.TaskRepo;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private SqlTransaction sqlTransaction;

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

    /*
        From Alexander
     */

    @Autowired
    private StudentQueryRepo queryRepo;

    @GetMapping("practice/task/{task}")
    public String taskById(
            @PathVariable Task task,
            Model model
    ) {
        model.addAttribute("task", task);
        return "practice/taskExecution";
    }

    @PostMapping("practice/task/{task}")
    public String saveStudentQuery(
            @PathVariable Task task,
            @RequestParam(name = "query") String query,
            Model model
    ) {
        // TODO: Check student's answer. Is it correct query or wrong?
        final StudentQuery stq = new StudentQuery();
        stq.setCorrect(true);
        stq.setQuery(query);
        stq.setTask(task);
        queryRepo.save(stq);
        model.addAttribute("task", task);
        return String.format("redirect:/practice/task/%d", task.getId());
    }
}
