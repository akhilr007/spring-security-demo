package com.example.spring_security.tutorial.spring_security.services;

import com.example.spring_security.tutorial.spring_security.dto.LoginDTO;
import com.example.spring_security.tutorial.spring_security.dto.SignupDTO;
import com.example.spring_security.tutorial.spring_security.dto.UserDTO;
import com.example.spring_security.tutorial.spring_security.models.User;
import com.example.spring_security.tutorial.spring_security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    public String login(LoginDTO loginDTO) {
        Authentication authentication =
                authenticationManager
                        .authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        loginDTO.getEmail(), loginDTO.getPassword()
                                )
                        );

        User user = (User) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }
}
