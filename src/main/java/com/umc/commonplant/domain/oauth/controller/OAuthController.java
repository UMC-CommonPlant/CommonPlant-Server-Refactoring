package com.umc.commonplant.domain.oauth.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.oauth.service.OAuthService;
import com.umc.commonplant.global.dto.JsonResponse;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OAuthController implements OAuthSwagger{
    private final OAuthService oAuthService;
    private final JwtService jwtService;

    @GetMapping("/login/{provider}")
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String provider){
        log.info("accessToken : " + accessToken);
        String token = oAuthService.oAuthLogin(accessToken, provider);
        String email = oAuthService.kakaoLogin(accessToken);
        if(token != null){
            return ResponseEntity.ok(new JsonResponse(true, 200, "login", token));
        }else{
            return ResponseEntity.ok(new JsonResponse(true, 2001, "no User Info", email));
        }
    }

    @GetMapping("/api/token")
    public ResponseEntity<JsonResponse> validToken(){
        String token = jwtService.getJwt();
        boolean isValid = jwtService.validateToken(token);

        return isValid ?
                ResponseEntity.ok(new JsonResponse(true, 200, "유효한 토큰입니다", isValid))
                : ResponseEntity.ok(new JsonResponse(false, 2004, "토큰을 확인하세요", null));
    }
}
