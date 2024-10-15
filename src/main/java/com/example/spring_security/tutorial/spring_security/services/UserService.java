package com.example.spring_security.tutorial.spring_security.services;

import com.example.spring_security.tutorial.spring_security.models.User;
import com.example.spring_security.tutorial.spring_security.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(
                () ->
                    new BadCredentialsException(
                            String.format("User with email %s found", username)
                    )
        );
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id"));
    }


    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
