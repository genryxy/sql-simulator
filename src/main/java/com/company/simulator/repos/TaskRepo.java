package com.company.simulator.repos;

import com.company.simulator.model.Task;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.PostUpdate;

public interface TaskRepo extends CrudRepository<Task, Long> {
    @Modifying
    @PostUpdate
    @Query(value = "= ?1",
            nativeQuery = true)
    void createTable(String query);
}
