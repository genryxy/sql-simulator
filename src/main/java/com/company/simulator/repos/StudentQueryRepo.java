package com.company.simulator.repos;

import com.company.simulator.model.StudentQuery;
import org.springframework.data.repository.CrudRepository;

public interface StudentQueryRepo extends CrudRepository<StudentQuery, Long> {
}
