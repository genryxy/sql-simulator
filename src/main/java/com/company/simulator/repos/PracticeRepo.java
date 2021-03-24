package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface PracticeRepo extends CrudRepository<Practice, Long> {
    @Modifying
    @Query(
            value = "INSERT INTO practice_deadlines (practice_id, date_start, date_end, after_deadline) values (:practiceId, :startDate, :endDate, :afterDeadline)",
            nativeQuery = true)
    @Transactional
    void addDeadLineToPractice(@Param("practiceId") Long practiceId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("afterDeadline") Boolean afterDeadline);
}
