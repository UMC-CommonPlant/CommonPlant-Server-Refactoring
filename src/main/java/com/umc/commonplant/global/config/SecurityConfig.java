package com.umc.commonplant.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/user")
                    .permitAll()
                .mvcMatchers(HttpMethod.GET,"/user/{name:^[0-9a-zA-Zㄱ-ㅎ가-힣 ]*$}/**")
                    .permitAll()
                .mvcMatchers(HttpMethod.GET, "/login/**")
                    .permitAll()
                .mvcMatchers(HttpMethod.GET, "/place/myGarden")
                    .permitAll()
                .mvcMatchers(HttpMethod.GET, "/info/**")
                    .permitAll()
                .mvcMatchers(HttpMethod.GET, "/word/**")
                    .permitAll()
                .mvcMatchers(HttpMethod.POST, "/place/create").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .build();
    }
}