package com.umc.commonplant.domain.user.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.user.dto.UserDto;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.OAuthService;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final OAuthService oAuthService;
    private final JwtService jwtService;

    @PostMapping("/user") //join
    public ResponseEntity<JsonResponse> join(@RequestPart("user") UserDto.join req, @RequestPart("image")MultipartFile image){
        log.info("join");
        String token = userService.joinUser(req, image);

        return ResponseEntity.ok(new JsonResponse(true, 200, "join", token));
    }
    @GetMapping("/login")
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String provider){
        log.info("accessToken" + accessToken);
//        System.out.println("accessToken: " + accessToken);
        String token = oAuthService.oAuthLogin(accessToken, provider);

        return ResponseEntity.ok(new JsonResponse(true, 200, "login", token));
    }
    @GetMapping("/user/{uuid}") // 회원정보 조회
    public ResponseEntity<JsonResponse> getUser(@PathVariable String uuid){
        //String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getUser", user));
    }
}
