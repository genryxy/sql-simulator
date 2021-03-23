package com.company.simulator.repos;

import com.company.simulator.model.Submission;
import org.springframework.data.repository.CrudRepository;

public interface SubmissionRepo extends CrudRepository<Submission, Long> {
}
