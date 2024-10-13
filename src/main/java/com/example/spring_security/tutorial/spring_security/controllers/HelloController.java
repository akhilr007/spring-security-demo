package com.example.spring_security.tutorial.spring_security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
