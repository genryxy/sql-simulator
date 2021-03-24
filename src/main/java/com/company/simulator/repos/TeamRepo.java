package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TeamRepo extends CrudRepository<Team, Long> {
    Optional<Team> findTeamByInvitation(String invitation);
    Optional<List<Team>> findTeamsByPracticesContains(Practice practice);
    Optional<List<Team>> findTeamsByPracticesNotContainsAndAuthorId(Practice practice, Long id);

    @Modifying
    @Query(
            value = "INSERT INTO practice_x_team (practice_id, team_id) values (:practiceId, :teamId)",
            nativeQuery = true)
    @Transactional
    void assignPracticeToTeam(@Param("practiceId") Long practiceId, @Param("teamId") Long teamId);

    @Modifying
    @Query(
            value = "DELETE FROM practice_x_team WHERE (practice_id = :practiceId AND team_id = :teamId)",
            nativeQuery = true)
    @Transactional
    void throwPracticeToTeam(@Param("practiceId") Long practiceId, @Param("teamId") Long teamId);
}
