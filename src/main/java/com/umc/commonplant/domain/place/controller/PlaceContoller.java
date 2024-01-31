package com.umc.commonplant.domain.place.controller;

import com.umc.commonplant.global.Jwt.JwtService;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.plant.service.PlantService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.domain.weather.service.WeatherService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.umc.commonplant.domain.plant.dto.PlantDto;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequestMapping("/place")
@RequiredArgsConstructor
@RestController
public class PlaceContoller implements PlaceSwagger{
    private final PlaceService placeService;
    private final UserService userService;
    private final JwtService jwtService;
    private final WeatherService weatherService;
    private final PlantService plantService;

    //장소 추가
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
        placeService.userOnPlace(user, code);
        PlaceDto.getPlaceRes res = placeService.getPlace(user, code);
        res.setPlantList(plantService.getMyGardenPlantList(code));
        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlace", res));
    }

    @GetMapping("/weather/{code}")
    public ResponseEntity<JsonResponse> getPlaceWeather(@PathVariable("code") String code)
    {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        PlaceDto.getPlaceGridRes placeGrid = placeService.getPlaceGrid(user, code);
        PlaceDto.getWeatherRes res = weatherService.getPlaceWeather(placeGrid);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlaceWeather", res));
    }
//    //친구 요청
    @PostMapping("/friends")
    public ResponseEntity<JsonResponse> newFriends(@RequestBody PlaceDto.newFriendsReq req){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String placeCode = placeService.newFriend(req.getName(), req.getCode());

        return ResponseEntity.ok(new JsonResponse(true, 200, "newFriend", placeCode));
    }

    //친구 검색
    @GetMapping("/friends")
    public ResponseEntity<JsonResponse> getFriends(@RequestBody PlaceDto.getFriendsReq req){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String inputName = req.getName();
        Optional<User> users = placeService.getFriends(inputName);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getFriends", users));
    }
    // 메인페이지
    @GetMapping("/myGarden")
    public ResponseEntity<JsonResponse> getMyGarden(){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        String name = user.getName();

        //placeList
        List<PlaceDto.getPlaceListRes> placeList = placeService.getPlaceList(user);
        //plantList
        List<PlantDto.getPlantListRes> plantList = plantService.getPlantList(user);
        //mainpage
        PlaceDto.getMainPage mainPage = new PlaceDto.getMainPage(name, placeList, plantList);


        return ResponseEntity.ok(new JsonResponse(true, 200, "getMyGarden", mainPage));
    }

    // 사용자가 속한 장소 리스트
    @GetMapping("/user")
    public ResponseEntity<JsonResponse> getPlaceBelongUser(){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        List<PlaceDto.getPlaceBelongUser> placeList = placeService.getPlaceBelongUser(user);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlaceBelongUser",placeList));
    }

    // 친구 리스트 조회
    @GetMapping("/{code}/friends")
    public ResponseEntity<JsonResponse> getPlaceFriends(@PathVariable String code){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        placeService.userOnPlace(user, code);
        List<PlaceDto.getPlaceFriends> userList = placeService.getPlaceFriends(code);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlaceFriends", userList));
    }
}
