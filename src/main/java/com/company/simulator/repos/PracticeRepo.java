package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PracticeRepo extends CrudRepository<Practice, Long> {

    @Modifying
    @Query(
        value = "INSERT INTO practice_deadlines (practice_id, date_start, date_end, after_deadline) values (:practiceId, :startDate, :endDate, :afterDeadline)",
        nativeQuery = true)
    @Transactional
    void addDeadLineToPractice(@Param("practiceId") Long practiceId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("afterDeadline") Boolean afterDeadline);

    @Query(
        value = "SELECT * FROM practice p WHERE p.author_id = ?1 AND p.id NOT IN (SELECT practice_id FROM practice_deadlines)",
        nativeQuery = true)
    Optional<List<Practice>> findAllPracticeNotInProcess(Long authorId);

    @Query(
        value = "SELECT * FROM practice p WHERE p.author_id = ?1 AND p.id IN (SELECT practice_id FROM practice_deadlines WHERE date_end < ?2)",
        nativeQuery = true)
    Optional<List<Practice>> findAllPracticeInArchive(Long authorId, LocalDateTime dateTime);

    @Query(
        value = "SELECT * FROM practice p WHERE p.author_id = ?1 AND p.id IN (SELECT practice_id FROM practice_deadlines WHERE date_end > ?2)",
        nativeQuery = true)
    Optional<List<Practice>> findAllPracticeInProcess(Long authorId, LocalDateTime dateTime);

    @Query(
        value = "SELECT * FROM practice p WHERE p.id IN ( " +
                "SELECT pt.practice_id FROM practice_x_team pt " +
                "JOIN student s ON pt.team_id = s.team_id " +
                "WHERE s.user_id = ?1) and p.id != ?2",
        nativeQuery = true
    )
    Optional<List<Practice>> findAllForUserExcept(Long userId, Long practiceId);

    @Query(
        value = "SELECT date_end FROM practice_deadlines where practice_id = ?1",
        nativeQuery = true)
    LocalDateTime getDeadlineByPracticeId(Long practiceId);

    @Modifying
    @Query(
        value = "update practice set " +
            "name = :name," +
            "description = :description " +
            "where id = :id",
        nativeQuery = true)
    @Transactional
    void updatePractice(@Param("id") Long id,
                        @Param("name") String name,
                        @Param("description") String description
    );

    @Modifying
    @Query(
        value = "update practice_deadlines set " +
            "date_end = :endDate," +
            "after_deadline = :afterDeadline " +
            "where practice_id = :practiceId",
        nativeQuery = true)
    @Transactional
    void updateDeadLineToPractice(@Param("practiceId") Long practiceId, @Param("endDate") LocalDateTime endDate, @Param("afterDeadline") Boolean afterDeadline);
}
