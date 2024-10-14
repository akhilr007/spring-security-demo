package com.example.spring_security.tutorial.spring_security.controllers;

import com.example.spring_security.tutorial.spring_security.dto.LoginDTO;
import com.example.spring_security.tutorial.spring_security.dto.SignupDTO;
import com.example.spring_security.tutorial.spring_security.dto.UserDTO;
import com.example.spring_security.tutorial.spring_security.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupDTO signupDTO){
        return new ResponseEntity<>(authService.signup(signupDTO), HttpStatus.CREATED) ;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO,
                                        HttpServletResponse response){
        String token = authService.login(loginDTO);

        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }
}
