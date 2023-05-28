package org.example.home.service;

import org.example.home.domain.entity.Role;
import org.example.home.domain.entity.User;
import org.example.home.domain.repository.UserRepository;
import org.example.home.dto.DtoRegister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServiceUser {

    private final UserRepository userRepository;

    public ServiceUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(DtoRegister dtoRegister, PasswordEncoder passwordEncoder, Role role) {
        User user = new User()
                .setEmail(dtoRegister.getEmail())
                .setUsername(dtoRegister.getUsername())
                .setPassword(passwordEncoder.encode(dtoRegister.getPassword()))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .setRole(role);

        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
