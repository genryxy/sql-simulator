package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepo extends CrudRepository<Submission, Long> {
    Optional<List<Submission>> findByUserAndPracticeAndTask(User user, Practice practice, Task task);

    Optional<List<Submission>> findByUserAndPractice(User user, Practice practice);

    Optional<List<Submission>> findByUserAndTask(User user, Task task);

    Optional<List<Submission>> findByPracticeAndTask(Practice practice, Task task);

    Optional<List<Submission>> findByUser(User user);

    @Query(
        value = "select (select count(sin.id) " +
            "       from submission sin " +
            "       where sin.is_correct and sin.task_id = ?1 and sin.user_id = ?2) as correct, " +
            "   count(s.id) as total " +
            "   from submission s " +
            "   where s.task_id = ?1 and s.user_id = ?2 ",
        nativeQuery = true)
    Map<String, Number> findNumberAttemptsForTaskAndUser(Long taskId, Long userId);

    @Query(
        value = "select (select count(sin.id) " +
                    "from submission sin " +
                    "where sin.is_correct and sin.task_id = ?1) as correct, " +
                    "count(s.id) as total " +
                    "from submission s " +
                    "where s.task_id = ?1 ",
        nativeQuery = true)
    Map<String, Integer> findNumberAttemptsForTask(Long taskId);

    @Query(
        value = "with task_points as ( " +
            "    select distinct s.task_id, points " +
            "    from submission s " +
            "    join task ts on s.task_id = ts.id " +
            "    where s.is_correct and s.practice_id = ?1 and s.user_id = ?2) " +
            " select coalesce(sum(points), 0) score, " +
            "    (select sum(points) as total " +
            "     from task t " +
            "     join practice_x_task pt on t.id = pt.task_id " +
            "     where pt.practice_id = ?1) " +
            "from task_points",
        nativeQuery = true
    )
    Map<String, Number> findScoreToTotalForPracticeByUser(Long practiceId, Long userId);

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
    int findPointsByPracticeAndUser(Long practiceId, Long userId);

}
