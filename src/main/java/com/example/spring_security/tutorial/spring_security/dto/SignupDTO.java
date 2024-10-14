package com.example.spring_security.tutorial.spring_security.dto;

import lombok.Data;

@Data
public class SignupDTO {

    private String email;
    private String password;
    private String name;
}
