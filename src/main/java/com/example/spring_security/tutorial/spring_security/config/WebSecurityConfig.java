package com.example.spring_security.tutorial.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests(auth ->
                    auth
                        .requestMatchers("/welcome", "/auth/**").permitAll()
                        .requestMatchers("/greet/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                .formLogin(Customizer.withDefaults()); // if not using form, dont use csrf and session

        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    UserDetailsService myInMemoryUserDetailsService(){
//
//        UserDetails adminUser = User
//                .withUsername("akhil")
//                .password(passwordEncoder().encode("test"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User
//                .withUsername("anuj")
//                .password(passwordEncoder().encode("test"))
//                .roles("DRIVER")
//                .build();
//
//        return new InMemoryUserDetailsManager(adminUser, user);
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
