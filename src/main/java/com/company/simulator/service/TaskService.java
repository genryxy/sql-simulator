package com.company.simulator.service;

import com.company.simulator.model.Task;
import com.company.simulator.repos.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public Optional<Task> getById(Long id){
        return taskRepo.findById(id);
    }

    public void save(Task practice){
        taskRepo.save(practice);
    }

    public List<Task> getAll(){
        return (List<Task>) taskRepo.findAll();
    }
}
