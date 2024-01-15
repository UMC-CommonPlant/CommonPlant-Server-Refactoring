package com.umc.commonplant.domain.oauth.controller;

import com.umc.commonplant.domain.oauth.service.OAuthService;
import com.umc.commonplant.global.dto.JsonResponse;
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
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/login/{provider}")
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String provider){
        log.info("accessToken : " + accessToken);
        String token = oAuthService.oAuthLogin(accessToken, provider);

        return ResponseEntity.ok(new JsonResponse(true, 200, "login", token));
    }
}
