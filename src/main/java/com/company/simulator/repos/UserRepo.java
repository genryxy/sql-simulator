package com.company.simulator.repos;

import com.company.simulator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

    @Query(
        value = "SELECT * FROM user u " +
            "JOIN practice_x_task p ON t.id=p.task_id " +
            "WHERE t.category_id=?1 and p.practice_id=?2",
        nativeQuery = true)
    Collection<User> findAllTeam(Long teamId);
}
