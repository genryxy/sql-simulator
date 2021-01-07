package com.company.simulator.controller;

import com.company.simulator.model.Message;
import com.company.simulator.model.User;
import com.company.simulator.repos.MessageRepo;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public final class MainController {

    @Autowired
    private MessageRepo msgRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        final Iterable<Message> msgs = msgRepo.findAll();
        model.put("messages", msgs);
        return "main";
    }

    @PostMapping("/main")
    public String add(
        @AuthenticationPrincipal User user,
        @RequestParam String text,
        @RequestParam String tag,
        Map<String, Object> model
    ) {
        final Message message = new Message(text, tag, user);
        msgRepo.save(message);
        final Iterable<Message> msgs = msgRepo.findAll();
        model.put("messages", msgs);
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        final Iterable<Message> msgs;
        if (filter != null && !filter.isEmpty()) {
            msgs = msgRepo.findByTag(filter);
        } else {
            msgs = msgRepo.findAll();
        }
        model.put("messages", msgs);
        return "main";
    }

}
