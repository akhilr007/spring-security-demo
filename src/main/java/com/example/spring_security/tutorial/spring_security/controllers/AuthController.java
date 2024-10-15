package com.example.spring_security.tutorial.spring_security.controllers;

import com.example.spring_security.tutorial.spring_security.dto.LoginDTO;
import com.example.spring_security.tutorial.spring_security.dto.LoginResponseDTO;
import com.example.spring_security.tutorial.spring_security.dto.SignupDTO;
import com.example.spring_security.tutorial.spring_security.dto.UserDTO;
import com.example.spring_security.tutorial.spring_security.services.AuthService;
import com.example.spring_security.tutorial.spring_security.services.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupDTO signupDTO){
        return new ResponseEntity<>(authService.signup(signupDTO), HttpStatus.CREATED) ;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO,
                                        HttpServletResponse response){

        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);

        Cookie accessToken = new Cookie("accessToken", loginResponseDTO.getAccessToken());
        accessToken.setHttpOnly(true);
        accessToken.setSecure("production".equals(deployEnv));
        response.addCookie(accessToken);

        Cookie refreshToken = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure("production".equals(deployEnv));
        response.addCookie(refreshToken);

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){

        String refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() ->
                            new AuthenticationServiceException("Refresh Token not found in cookies")
                    );

        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() ->
                        new AuthenticationServiceException("Refresh Token not found in cookies")
                );

        sessionService.deleteSessionByToken(refreshToken);

        Cookie accessToken = new Cookie("accessToken", null);
        accessToken.setMaxAge(0); // This deletes the cookie
        accessToken.setHttpOnly(true);
        response.addCookie(accessToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0); // This deletes the cookie
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}
