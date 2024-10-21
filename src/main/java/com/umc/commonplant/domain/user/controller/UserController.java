package com.umc.commonplant.domain.user.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.user.dto.UserDto;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.core.util.Json;
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
    public ResponseEntity<JsonResponse> join(@RequestPart("user") UserDto.join req, @RequestParam(value ="image", required = false)MultipartFile image){
        log.info("[API] Join User");
        String token = userService.joinUser(req, image);

        return ResponseEntity.ok(new JsonResponse(true, 200, "join", token));
    }
    @GetMapping("/user/{name}") // 회원정보 조회
    public ResponseEntity<JsonResponse> getUserByName(@PathVariable String name){
        log.info("[API] Get User Info");
        User user = userService.getUserByName(name);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getUser", user));
    }
    @GetMapping("/user/{name}/exists")
    public ResponseEntity<JsonResponse> checkNameDuplicate(@PathVariable String name){
        log.info("[API] Check Duplicated Name");
        boolean checkName = userService.checkNameDuplication(name);

        return ResponseEntity.ok(new JsonResponse(true, 200, "사용가능한 이름입니다.", checkName));
    }

    @GetMapping("/user/profileImg")
    public ResponseEntity<JsonResponse> getUserProfileImg(){
        log.info("[API] Get User Profile Img");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String profileImg = userService.getUserProfileImage(user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "profile_img", profileImg));
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<JsonResponse> deleteUser() {
        log.info("[API] Delete User");
        String uuid = jwtService.resolveToken();
        userService.deleteUser(uuid);

        return ResponseEntity.ok(new JsonResponse(true, 200, "delete", uuid));
    }

    @PostMapping(value = "/user/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateUser(@RequestPart("user") UserDto.updateUserDto req, @RequestParam(value ="image", required = false)MultipartFile image){
        log.info("[API] Update User");
        String uuid = jwtService.resolveToken();
        userService.updateUser(uuid, req, image);

        return ResponseEntity.ok(new JsonResponse(true, 200, "update", null));
    }
}
