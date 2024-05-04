package com.umc.commonplant.global.security;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.Jwt.filter.JwtAuthenticationFilter;
import com.umc.commonplant.global.config.oauth.OAuth2AuthorizationRequestRepository;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.config.oauth.OAuth2SuccessHandler;
import com.umc.commonplant.global.config.oauth.OAuth2UserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
//@EnableWebSecurity(debug = true)
public class SecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final UserService userService;
    private final JwtService jwtService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return(web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/static**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(auth -> auth // 인증, 인가 설정
                        .requestMatchers(
                                new AntPathRequestMatcher("/user"),
                                new AntPathRequestMatcher("/user/{name:^[0-9a-zA-Zㄱ-ㅎ가-힣 ]*$}/**"),
                                new AntPathRequestMatcher("/login/**"),
                                new AntPathRequestMatcher("/place/myGarden"),
                                new AntPathRequestMatcher("/info/**"),
                                new AntPathRequestMatcher("/word/**")
                        ).permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.authorizationRequestRepository(oAuth2AuthorizationRequestRepository()))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService))
                        .successHandler(oAuth2SuccessHandler())
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**") // 로 시작하는 경우 401
                        ))
                .build();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        return new OAuth2SuccessHandler(jwtService,
                oAuth2AuthorizationRequestRepository(),
                userService
        );
    }

    @Bean
    public JwtAuthenticationFilter tokenAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository(){
        return new OAuth2AuthorizationRequestRepository();
    }

}
