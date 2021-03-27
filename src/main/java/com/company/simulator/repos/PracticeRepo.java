package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
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

    Optional<List<Practice>> findAllByAuthorId(Long authorId);
}
