package com.umc.commonplant.domain.place.controller;

import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface PlaceSwagger {

    @Operation(summary = "createPlace", description = "장소 생성")
    public ResponseEntity<JsonResponse> createPlace(
            @Parameter(description = "장소 생성 요청 정보", required = true) @RequestPart("place") PlaceDto.createPlaceReq req,
            @Parameter(description = "장소 대표 이미지") @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(summary = "updatePlace", description = "장소 수정")
    public ResponseEntity<JsonResponse> updatePlace(
            @Parameter(description = "place code", example = "XFGEDS") @PathVariable("code") String code,
            @Parameter(description = "장소 수정 요청 정보", required = true) @RequestPart("place") PlaceDto.updatePlaceReq updatePlaceReq,
            @Parameter(description = "장소 대표 이미지") @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(summary = "getPlace", description = "장소 코드로 장소 정보 조회")
    public ResponseEntity<JsonResponse> getPlace(
            @Parameter(description = "place code", example = "XFGEDS") @PathVariable("code") String code
    );

    @Operation(summary = "getPlaceWeather", description = "장소코드로 장소의 현재 날씨 조회")
    public ResponseEntity<JsonResponse> getPlaceWeather(
            @Parameter(description = "place code", example = "XFGEDS") @PathVariable("code") String code
    );

    @Operation(summary = "newFriends", description = "장소에 유저 추가")
    public ResponseEntity<JsonResponse> newFriends(@RequestBody PlaceDto.newFriendsReq req);

//    @Operation(summary = "getFriends", description = "친구 검색")
//    public ResponseEntity<JsonResponse> getFriends(@RequestBody PlaceDto.getFriendsReq req);

    @Operation(summary = "getMyGarden", description = "메인페이지: 유저가 가입되어있는 장소/식물 조회")
    public ResponseEntity<JsonResponse> getMyGarden();

    @Operation(summary = "getPlaceBelongUser", description = "유저가 속한 장소 리스트 조회")
    public ResponseEntity<JsonResponse> getPlaceBelongUser();

    @Operation(summary = "getPlaceFriends", description = "장소에 속한 유저 리스트 조회")
    public ResponseEntity<JsonResponse> getPlaceFriends(
            @Parameter(description = "place code", example = "XFGEDS") @PathVariable String code
    );

    }
