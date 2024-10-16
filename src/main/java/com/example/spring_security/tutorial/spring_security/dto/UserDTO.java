package com.example.spring_security.tutorial.spring_security.dto;

import com.example.spring_security.tutorial.spring_security.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private Set<Role> roles;
}
