package com.example.spring_security.tutorial.spring_security.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String refreshToken;

    @ManyToOne
    private User user;

    @CreationTimestamp
    private LocalDateTime lastUsedAt;
}
