package com.company.simulator.repos;

import com.company.simulator.model.Team;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepo extends CrudRepository<Team, Long> {
    Optional<Team> findTeamByInvitation(String invitation);
}
