package com.company.simulator.repos;

import com.company.simulator.model.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepo extends CrudRepository<Student, Long> {
    Optional<List<Student>> findAllByUserId(Long user);
}
