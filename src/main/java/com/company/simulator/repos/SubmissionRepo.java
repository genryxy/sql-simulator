package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepo extends CrudRepository<Submission, Long> {
    Optional<List<Submission>> findByUserAndPracticeAndTask(User user, Practice practice, Task task);

    Optional<List<Submission>> findByUserAndPractice(User user, Practice practice);

    Optional<List<Submission>> findByPracticeAndTask(Practice practice, Task task);

    Optional<List<Submission>> findByUser(User user);

    @Query(
        value = " with tas as (\n" +
            "    select distinct points, task_id from task t\n" +
            "    join submission s on s.task_id = t.id\n" +
            "    where s.practice_id = ?1\n" +
            "      and s.user_id = ?2\n" +
            "      and s.is_correct\n" +
            "    )\n" +
            "select coalesce(sum(points), 0) from (select points from tas) as res",
        nativeQuery = true)
    int findPointsByUserAndPractice(Long practiceId, Long userId);

}
