package com.example.spring_security.tutorial.spring_security.services;

import com.example.spring_security.tutorial.spring_security.dto.LoginDTO;
import com.example.spring_security.tutorial.spring_security.dto.SignupDTO;
import com.example.spring_security.tutorial.spring_security.dto.UserDTO;
import com.example.spring_security.tutorial.spring_security.models.User;
import com.example.spring_security.tutorial.spring_security.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                    new UsernameNotFoundException(
                            String.format("User with email %s found", username)
                    )
        );
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id"));
    }



}
