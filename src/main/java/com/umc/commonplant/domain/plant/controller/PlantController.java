package com.umc.commonplant.domain.plant.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.domain.plant.service.PlantService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PlantController implements PlantSwagger {

    private final UserService userService;
    private final JwtService jwtService;
    private final PlaceService placeService;
    private final PlantService plantService;

    /**
     * [POST] 식물 추가
     * [POST] /plant/add
     * @return 추가한 식물의 애칭
     */
    @PostMapping(value = "/plant/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResponse> createPlant(@RequestPart("plant") PlantDto.createPlantReq createPlantReq,
                                                    @RequestPart("image") MultipartFile file) {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String nickname = plantService.createPlant(user, createPlantReq, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "createPlant", nickname));
    }

    /**
     * [GET] 식물 조회
     * @return 식물 객체
     */
    @GetMapping("/plant/{plantIdx}")
    public ResponseEntity<JsonResponse> getPlantCard(@PathVariable Long plantIdx) {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        PlantDto.getPlantRes plant = plantService.getPlant(user, plantIdx);

        // TODO: 추후에 메모 리스트를 포함해서 반환되도록 수정
        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlant", plant));
    }

    /**
     * [GET] 같은 사람이 키우는 식물 리스트 조회
     * @return 같은 사람이 키우는 식물 리스트
     */
    @GetMapping("/user/plantList")
    public ResponseEntity<JsonResponse> getPlantList() {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        List<PlantDto.getPlantListRes> plantList = plantService.getPlantList(user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlantList", plantList));
    }

//    /**
//     * [GET] 같은 장소에 있는 식물 리스트 조회
//     * @param placeCode: 장소 코드
//     * @return 같은 장소에 있는 식물 리스트
//     */
//    @GetMapping("/place/plantList/{placeCode}")
//    public ResponseEntity<JsonResponse> getMyGardenPlantList(@PathVariable String placeCode) {
//        String uuid = jwtService.resolveToken();
//        User user = userService.getUser(uuid);
//
//        // Place place = placeService.getPlace(user, placeCode);
//        String place = placeService.getPlace(user, placeCode).getCode();
//
//        List<PlantDto.getMyGardenPlantListRes> plantList = plantService.getMyGardenPlantList(place);
//
//        return ResponseEntity.ok(new JsonResponse(true, 200, "getMyGardenPlantList", plantList));
//    }

    /**
     * [PUT] 식물의 D-Day 업데이트
     * @param plantIdx
     * @return
     */
    @PutMapping("/plant/update/wateredDate/{plantIdx}")
    public ResponseEntity<JsonResponse> updateWateredDate(@PathVariable Long plantIdx){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String nickname = plantService.updateWateredDate(user, plantIdx);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updateWateredDate", nickname));
    }

    /**
     * [GET] /plant/update
     * @return 수정하기 전 식물의 애칭
     */
    @GetMapping("/plant/update/{plantIdx}")
    public ResponseEntity<JsonResponse> getUpdatedPlant(@PathVariable Long plantIdx){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String nickname = plantService.getUpdatedPlant(user, plantIdx).getNickname();

        return ResponseEntity.ok(new JsonResponse(true, 200, "getUpdatedPlant", nickname));
    }

    /**
     * [PUT] /plant/update
     * @return 수정한 식물의 애칭
     */
    @PutMapping(value = "/plant/update/{plantIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResponse> updatePlant(@PathVariable Long plantIdx,
                                                    @RequestPart("plant") PlantDto.updatePlantReq updatePlantReq,
                                                    @RequestPart("image") MultipartFile file){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String updatedPlant = plantService.updatePlant(user, plantIdx, updatePlantReq, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updatePlant", updatedPlant));
    }

    /**
     * [DELETE] /plant/delete
     * @param plantIdx
     * @return 삭제한 식물의 닉네임
     */
    @DeleteMapping("/plant/delete/{plantIdx}")
    public ResponseEntity<JsonResponse> deletePlant(@PathVariable Long plantIdx){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String deletedPlant = plantService.deletePlant(user, plantIdx);

        return ResponseEntity.ok(new JsonResponse(true, 200, "deletePlant", deletedPlant));
    }

}
