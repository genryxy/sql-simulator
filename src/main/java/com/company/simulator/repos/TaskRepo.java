package com.company.simulator.repos;

import com.company.simulator.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskRepo extends CrudRepository<Task, Long> {
    Optional<Task> getById(Long taskId);
}
