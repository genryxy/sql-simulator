package com.company.simulator;

import com.company.simulator.model.Message;
import com.company.simulator.repos.MessageRepo;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public final class GreetingController {

    @Autowired
    private MessageRepo msgrepo;

    @GetMapping("/greeting")
    public String greeting(
        @RequestParam(name="name", required=false, defaultValue="World") String name,
        Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping
    public String main(Map<String, Object> model) {
        final Iterable<Message> msgs = msgrepo.findAll();
        model.put("messages", msgs);
        return "main";
    }

    @PostMapping
    public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        final Message message = new Message(text, tag);
        msgrepo.save(message);
        final Iterable<Message> msgs = msgrepo.findAll();
        model.put("messages", msgs);
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        final Iterable<Message> msgs;
        if (filter != null && !filter.isEmpty()) {
            msgs = msgrepo.findByTag(filter);
        } else {
            msgs = msgrepo.findAll();
        }
        model.put("messages", msgs);
        return "main";
    }

}
