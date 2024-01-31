package com.umc.commonplant.domain.user.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.user.dto.UserDto;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController implements  UserSwagger{
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) //join
    public ResponseEntity<JsonResponse> join(@RequestPart("user") UserDto.join req, @RequestPart(value ="image", required = false)MultipartFile image){
        log.info("join");
        String token = userService.joinUser(req, image);

        return ResponseEntity.ok(new JsonResponse(true, 200, "join", token));
    }
    @GetMapping("/user/{name}") // 회원정보 조회
    public ResponseEntity<JsonResponse> getUserByName(@PathVariable String name){
        User user = userService.getUserByName(name);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getUser", user));
    }
    @GetMapping("/user/{name}/exists")
    public ResponseEntity<JsonResponse> checkNameDuplicate(@PathVariable String name){
        boolean checkName = userService.checkNameDuplication(name);

        return ResponseEntity.ok(new JsonResponse(true, 200, "사용가능한 이름입니다.", checkName));
    }
}
