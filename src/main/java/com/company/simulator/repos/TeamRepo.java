package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Team;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TeamRepo extends CrudRepository<Team, Long> {
    Optional<Team> findTeamByInvitation(String invitation);

    Optional<List<Team>> findTeamsByPracticesContains(Practice practice);

    Optional<List<Team>> findTeamsByPracticesNotContainsAndAuthorId(Practice practice, Long id);

    Optional<List<Team>> findTeamsByAuthorIdAndIdNot(Long authorId, Long id);

    @Query(
        value = "SELECT * FROM team t " +
            "JOIN student s on t.id = s.team_id " +
            "WHERE s.user_id = ?1",
        nativeQuery = true
    )
    Optional<List<Team>> findTeamsByStudentId(Long student);

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

    @Modifying
    @Query(
        value = "update team set " +
            "name = :name " +
            "where id = :id",
        nativeQuery = true)
    @Transactional
    void updateTeam(@Param("id") Long id,
                    @Param("name") String name
    );
}
