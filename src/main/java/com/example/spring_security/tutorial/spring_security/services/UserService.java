package com.example.spring_security.tutorial.spring_security.services;

import com.example.spring_security.tutorial.spring_security.dto.SignupDTO;
import com.example.spring_security.tutorial.spring_security.dto.UserDTO;
import com.example.spring_security.tutorial.spring_security.models.User;
import com.example.spring_security.tutorial.spring_security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

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

    public UserDTO signup(SignupDTO signupDTO) {
        Optional<User> user = userRepository.findByEmail(signupDTO.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User already exists");
        }

        User toBeCreatedUser = mapper.map(signupDTO, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));
        User newUser = userRepository.save(toBeCreatedUser);
        return mapper.map(newUser, UserDTO.class);

    }
}
