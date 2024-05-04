package com.umc.commonplant.domain.Jwt.filter;

import com.umc.commonplant.domain.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final static String HEADER_TOKEN =  "X-AUTH-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws
            ServletException, IOException {
        String token = jwtService.resolveTokenForFilter();
        System.out.println("abcde");
        log.info(token);
        // 가져온 토큰이 유효한지 확인하고, 유효할 때는 인증정보를 설정
//        if(jwtService.validateToken(token)){
        Authentication authentication = jwtService.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
        filterChain.doFilter(request, response);
    }
    private String getAccessToken(String authorizationHeader){
        if(authorizationHeader != null && authorizationHeader.startsWith(HEADER_TOKEN)){
            return authorizationHeader.substring(HEADER_TOKEN.length());
        }
        return null;
    }

}