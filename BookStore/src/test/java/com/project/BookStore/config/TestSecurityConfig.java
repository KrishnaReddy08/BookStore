package com.project.BookStore.config;

import com.project.BookStore.JWT.jwtservice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.mockito.Mockito.mock;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig{
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth->{
                    auth.anyRequest().permitAll();
                })
                .build();
         }
         @Bean
         public jwtservice jwtService() {
        return mock(jwtservice.class);
    }
}