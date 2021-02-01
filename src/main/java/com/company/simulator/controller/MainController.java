package com.company.simulator.controller;

import com.company.simulator.model.Message;
import com.company.simulator.model.User;
import com.company.simulator.repos.MessageRepo;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public final class MainController {

    @Autowired
    private MessageRepo msgRepo;

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        final Iterable<Message> msgs;
        if (filter != null && !filter.isEmpty()) {
            msgs = msgRepo.findByTag(filter);
        } else {
            msgs = msgRepo.findAll();
        }
        model.addAttribute("messages", msgs);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
        @AuthenticationPrincipal User user,
        @Valid Message message,
        BindingResult bindingResult,
        Model model
    ) {
        // Important that param `bindingResult` should be before `model`.
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            final Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", null);
            msgRepo.save(message);
        }
        final Iterable<Message> msgs = msgRepo.findAll();
        model.addAttribute("messages", msgs);
        return "main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
        @AuthenticationPrincipal User currentUser,
        @PathVariable User user,
        Model model,
        @RequestParam(required = false) Message message
    ) {
        model.addAttribute("messages", user.getMessages());
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
        @AuthenticationPrincipal User currentUser,
        @PathVariable Long user,
        @RequestParam(name = "id") Message message,
        @RequestParam(name = "text") String text,
        @RequestParam(name = "tag") String tag
    ) {
        // TODO: Return message in case of attempt
        // to change someone else's message.
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            msgRepo.save(message);
        }

        return String.format("redirect:/user-messages/%d", user);
    }

}
