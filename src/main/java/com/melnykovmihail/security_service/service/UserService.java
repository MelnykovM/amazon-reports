package com.melnykovmihail.security_service.service;

import com.melnykovmihail.security_service.data.entity.User;
import com.melnykovmihail.security_service.data.entity.enums.Role;
import com.melnykovmihail.security_service.data.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("A user with the same name already exists");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("A user with this email already exists");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
