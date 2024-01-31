package com.umc.commonplant.global.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return(web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/static**"));
    }
    // 특정 Http 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeRequests(auth -> auth // 인증, 인가 설정
                        .requestMatchers(
                                new AntPathRequestMatcher("/user"),
                                new AntPathRequestMatcher("/user/{name:^[0-9a-zA-Zㄱ-ㅎ가-힣 ]*$}/**"),
                                new AntPathRequestMatcher("/login/**"),
                                new AntPathRequestMatcher("place/myGarden"),
                                new AntPathRequestMatcher("/info/**"),
                                new AntPathRequestMatcher("/word/**")
                        ).permitAll()
                .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}