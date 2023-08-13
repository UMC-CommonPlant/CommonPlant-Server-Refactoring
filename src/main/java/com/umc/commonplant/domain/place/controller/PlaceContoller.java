package com.umc.commonplant.domain.place.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import com.umc.commonplant.global.utils.openAPI.KakaoOpenAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Slf4j
@RequestMapping("/place")
@RequiredArgsConstructor
@RestController
public class PlaceContoller {
    private final PlaceService placeService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<JsonResponse> createPlace(@RequestPart("place") PlaceDto.create req, @RequestPart("image") MultipartFile image)
    {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        String placeCode = placeService.create(user, req, image);
        return ResponseEntity.ok(new JsonResponse(true, 200, "createPlace", placeCode));
    }

}
