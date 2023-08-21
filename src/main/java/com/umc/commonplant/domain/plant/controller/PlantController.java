package com.umc.commonplant.domain.plant.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain.plant.service.PlantService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PlantController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PlaceService placeService;
    private final PlantService plantService;

    /**
     * [POST] 식물 추가
     * [POST] /plant/add
     * @return 추가한 식물의 애칭
     */
    @PostMapping("/plant/add")
    public ResponseEntity<JsonResponse> createPlant(@RequestPart("plant") PlantDto.createPlantReq createPlantReq,
                                                    @RequestPart("image") MultipartFile file) {

        log.info("=============CREATE PLANT===============");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String nickname = plantService.createPlant(user, createPlantReq, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "createPlant", nickname));
    }

    /**
     * [GET] 같은 장소에 있는 식물 리스트 조회
     * @param placeCode: 장소 코드
     * @return 같은 장소에 있는 식물 리스트
     */
    @GetMapping("/plant/plantList/{placeCode}")
    public ResponseEntity<JsonResponse> getPlantList(@PathVariable String placeCode) {

        log.info("=============GET PLANT LIST===============");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        // Place place = placeService.getPlace(user, placeCode);
        String place = placeService.getPlace(user, placeCode).getCode();

        List<Plant> plantList = plantService.getPlantList(place);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlantList", plantList));
    }

    /**
     * [PATCH] /plant/update
     * @return 수정한 식물의 애칭
     */
    @PatchMapping("/plant/update/{plantIdx}")
    public ResponseEntity<JsonResponse> updatePlant(@PathVariable Long plantIdx,
                                                    @RequestPart("nickname") String nickname,
                                                    @RequestPart("image") MultipartFile file){

        log.info("=============UPDATE PLANT===============");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String updatedPlant = plantService.updatePlant(plantIdx, nickname, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updatePlant", updatedPlant));
    }
}
