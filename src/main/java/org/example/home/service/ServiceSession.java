package org.example.home.service;

import org.example.home.domain.entity.Session;
import org.example.home.domain.entity.User;
import org.example.home.domain.repository.SessionRepository;
import org.example.home.security.Generator;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class ServiceSession {

    public final SessionRepository sessionRepository;

    public ServiceSession(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void saveSession(User user, Generator generator) {
        Session session = new Session()
                .setUser(user)
                .setSessionToken(generator.generateToken(user.getId().toString()))
                .setExpiresAt(generator.expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        sessionRepository.save(session);
    }
}
