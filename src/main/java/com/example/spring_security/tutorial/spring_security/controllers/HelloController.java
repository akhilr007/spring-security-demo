package com.example.spring_security.tutorial.spring_security.controllers;

import com.example.spring_security.tutorial.spring_security.models.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class HelloController {


    @GetMapping("/")
    public String home(){
        return "Welcome to my home page!!!.";
    }

    @GetMapping("/greet")
    public String greet(){
        return "Welcome to my application";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome Users";
    }

    @GetMapping("/messages")
    public List<String> welcomeMessages(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("User {}", user);
        return List.of("Hello, everyone", "Welcome here");
    }
}
