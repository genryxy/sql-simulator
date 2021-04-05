package com.company.simulator.repos;

import com.company.simulator.model.Student;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends CrudRepository<Student, Long> {
    Optional<List<Student>> findAllByUserId(Long user);

    @Transactional
    void deleteByUserAndTeam(User user, Team team);

    @Modifying
    @Query(
        value = "INSERT INTO student (user_id, team_id) values (:userId, :teamId)",
        nativeQuery = true)
    @Transactional
    void addUserToTeam(
        @Param("userId") Long userId,
        @Param("teamId") Long teamId
    );
}
