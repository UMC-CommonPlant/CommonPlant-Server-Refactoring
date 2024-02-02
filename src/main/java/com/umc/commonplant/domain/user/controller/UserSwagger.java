package com.umc.commonplant.domain.user.controller;

import com.umc.commonplant.domain.user.dto.UserDto;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Controller", description = "유저 관련 API")
public interface UserSwagger {
    @Operation(summary = "회원가입", description = "회원가입 API호출")
    public ResponseEntity<JsonResponse> join(
            @Parameter(
                    name = "req",
                    description = "userReqeust 객체",
                    required = true
            )@RequestPart("req") UserDto.join req,
            @Parameter(
                    name = "image",
                    description = "프로필 이미지"
            )@RequestPart(value = "image", required = false)MultipartFile image
            );
    @Operation(summary = "유저 조회", description = "유저조회 API 호출")
    @Parameter(name = "name", description="조회할 유저 이름",example = "김커먼", required = true)
    public ResponseEntity<JsonResponse> getUserByName(@PathVariable String name);

    @Operation(summary = "이름 중복검사", description = "이름 중복검사 API 호출")
    @Parameter(name = "name", description = "중복 검사할 이름", example = "김커먼", required = true)
    public ResponseEntity<JsonResponse>  checkNameDuplicate(@PathVariable String name);
}
