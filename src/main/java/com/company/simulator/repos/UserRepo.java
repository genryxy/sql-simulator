package com.company.simulator.repos;

import com.company.simulator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(final String username);

}
