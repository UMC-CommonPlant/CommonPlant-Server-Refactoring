package com.umc.commonplant.domain.place.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import com.umc.commonplant.global.utils.openAPI.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;


@Slf4j
@RequestMapping("/place")
@RequiredArgsConstructor
@RestController
public class PlaceContoller {
    private final PlaceService placeService;
    private final UserService userService;
    private final JwtService jwtService;

    private final OpenApiService openApiService;

    @PostMapping("/create")
    public ResponseEntity<JsonResponse> createPlace(@RequestPart("place") PlaceDto.createPlaceReq req, @RequestPart("image") MultipartFile image)
    {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        String placeCode = placeService.create(user, req, image);
        return ResponseEntity.ok(new JsonResponse(true, 200, "createPlace", placeCode));
    }

    @GetMapping("/{code}")
    public ResponseEntity<JsonResponse> getPlace(@PathVariable("code") String code)
    {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        PlaceDto.getPlaceRes res = placeService.getPlace(user, code);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlace", res));
    }

    @GetMapping("/testOpenAPI")
    public Object testOpenAPI(){
        openApiService.getTime();
        return "test";
    }

}
