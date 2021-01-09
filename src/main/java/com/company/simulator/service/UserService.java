package com.company.simulator.service;

import com.company.simulator.model.Role;
import com.company.simulator.model.User;
import com.company.simulator.repos.UserRepo;
import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Value("${url.activation.path}")
    private String urlForActivation;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        final User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);
        if (!StringUtils.isEmpty(user.getEmail())) {
            final String message = String.format(
                "Hello, %s!\nWelcome to SQL-simulator. Please, follow the link: %s%s",
                user.getUsername(),
                urlForActivation,
                user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
        return true;
    }

    public boolean activateUser(final String code) {
        final User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }
}
