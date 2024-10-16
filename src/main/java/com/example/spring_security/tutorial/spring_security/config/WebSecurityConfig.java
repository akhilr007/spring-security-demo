package com.example.spring_security.tutorial.spring_security.config;

import com.example.spring_security.tutorial.spring_security.filters.JwtAuthFilter;
import com.example.spring_security.tutorial.spring_security.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.spring_security.tutorial.spring_security.enums.Permission.ADMIN_VIEW;
import static com.example.spring_security.tutorial.spring_security.enums.Role.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests(auth ->
                    auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/messages").hasRole(ADMIN.name())
                            .requestMatchers(HttpMethod.GET, "/welcome").hasAuthority(ADMIN_VIEW.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                .oauth2Login(oauth2Config -> oauth2Config
//                        .failureUrl("/login?error=true")
//                        .successHandler(oAuth2SuccessHandler)
//                );
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
