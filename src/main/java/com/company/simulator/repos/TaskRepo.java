package com.company.simulator.repos;

import com.company.simulator.model.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface TaskRepo extends CrudRepository<Task, Long> {

    @Query(
            value = "SELECT * FROM task t WHERE t.author_id = ?1",
            nativeQuery = true)
    Collection<Task> findAllTaskByAuthorId(Long id);

    @Modifying
    @Query(
            value = "INSERT INTO practice_x_task (practice_id, task_id) values (:practiceId, :taskId)",
            nativeQuery = true)
    @Transactional
    void addTaskToPractice(@Param("practiceId") Long practiceId, @Param("taskId") Long taskId);

    @Query(
            value = "SELECT * FROM task t " +
                    "JOIN practice_x_task p ON t.id=p.task_id " +
                    "WHERE t.category_id=?1 and p.practice_id=?2",
            nativeQuery = true)
    Collection<Task> findAllByCategoryAndPractice(Long category, Long practice);

    @Query(
        value = "select * from task t " +
            "   where t.id in ( " +
            "     select distinct task_id from student s " +
            "     join practice_x_team pt on pt.team_id = s.team_id " +
            "     join practice_x_task ptsk on pt.practice_id = ptsk.practice_id " +
            "     where s.user_id = ?1 )",
        nativeQuery = true
    )
    Optional<List<Task>> findAllForUser(Long userId);

    @Modifying
    @Query(
            value = "update task set " +
                    "author_id = :authorId," +
                    "name = :nameTask," +
                    "text = :text," +
                    "ddl_script = :ddlScript," +
                    "correct_query = :correctQuery," +
                    "points = :points," +
                    "is_private = :isPrivate," +
                    "category_id = :categoryId " +
                    "where id = :id",
            nativeQuery = true)
    @Transactional
    void updateTask(@Param("id") Long id,
                    @Param("authorId") Long authorId,
                    @Param("nameTask") String nameTask,
                    @Param("text") String text,
                    @Param("ddlScript") String ddlScript,
                    @Param("correctQuery") String correctQuery,
                    @Param("points") Integer points,
                    @Param("isPrivate") Boolean isPrivate,
                    @Param("categoryId") Long categoryId
    );
}
