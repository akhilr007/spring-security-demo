package com.example.spring_security.tutorial.spring_security.repositories;

import com.example.spring_security.tutorial.spring_security.models.Session;
import com.example.spring_security.tutorial.spring_security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
