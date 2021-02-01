package com.company.simulator.repos;

import com.company.simulator.model.Message;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Integer> {

    List<Message> findByTag(final String tag);

}
