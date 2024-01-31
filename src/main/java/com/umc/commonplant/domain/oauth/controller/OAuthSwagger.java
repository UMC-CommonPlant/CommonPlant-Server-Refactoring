package com.umc.commonplant.domain.oauth.controller;

import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "OAuth Controller", description = "소셜로그인 관련 API")
public interface OAuthSwagger {
    @Operation(summary = "소셜 로그인", description = "소셜로그인 API호출")
    @Parameter(name = "accessToken", description = "OAuth 서버가 발급한 액세스토큰", required = true)
    @Parameter(name = "provider", description = "소셜로그인", example = "kakao", required = true)
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String provider);
}
