package com.umc.commonplant.domain.place.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.friend.dto.FriendDto;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.domain.plant.service.PlantService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.domain.weather.service.WeatherService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RequestMapping("/place")
@RequiredArgsConstructor
@RestController
public class PlaceController implements PlaceSwagger{
    private final PlaceService placeService;
    private final UserService userService;
    private final JwtService jwtService;
    private final WeatherService weatherService;
    private final PlantService plantService;


    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(){
        log.info("[API] healthCheck");
        return ResponseEntity.ok("health check ok");
    }


    //장소 추가
    @PostMapping(value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> createPlace(@RequestPart("place") PlaceDto.createPlaceReq req, @RequestPart(value = "image", required = false) MultipartFile image)
    {
        log.info("[API] createPlace");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        String placeCode = placeService.create(user, req, image);
        return ResponseEntity.ok(new JsonResponse(true, 200, "createPlace", placeCode));
    }
    @PostMapping(value = "/friend/request",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> createPlaceAndSendFriendRequest(@RequestPart("place") PlaceDto.createPlaceReq placeReq, @RequestPart(value= "image", required = false) MultipartFile image, @RequestPart("friendList")FriendDto.sendFriendReq friendReq){
        //장소 코드 필요
        log.info("[API] registerPlace and Send FriendReq");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        FriendDto.placeCodeAndFriendResponse  res = placeService.registerPlace(user, placeReq, image, friendReq); // 중첩 API 호출

        return ResponseEntity.ok(new JsonResponse(true, 200, "creratePlaceAndSendFriendRequest", res));
    }

    @GetMapping("/{code}")
    public ResponseEntity<JsonResponse> getPlace(@PathVariable("code") String code)
    {
        log.info("[API] getPlace");
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
        log.info("[API] getPlaceWeather");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        PlaceDto.getPlaceGridRes placeGrid = placeService.getPlaceGrid(user, code);
        PlaceDto.getWeatherRes res = weatherService.getPlaceWeather(placeGrid);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlaceWeather", res));
    }
//    //친구 요청 수락 및 장소 초대
    @PostMapping("/friend/response")
    public ResponseEntity<JsonResponse> acceptFriendAndInvitedPlace(@RequestBody PlaceDto.acceptFriendsReq req){
        log.info("[API] accept Friend And Invited place");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String placeCode = placeService.acceptFriendRequest(req.getSender(), user.getName());

        return ResponseEntity.ok(new JsonResponse(true, 200, "acceptFriend and Invited place", placeCode));
    }

//    //친구 검색
//    @GetMapping("/friends")
//    public ResponseEntity<JsonResponse> getFriends(@RequestBody PlaceDto.getFriendsReq req){
//        String uuid = jwtService.resolveToken();
//        User user = userService.getUser(uuid);
//
//        String inputName = req.getName();
//        Optional<User> users = placeService.getFriends(inputName);
//
//        return ResponseEntity.ok(new JsonResponse(true, 200, "getFriends", users));
//    }
    // 메인페이지
    @GetMapping("/myGarden")
    public ResponseEntity<JsonResponse> getMyGarden(){
        log.info("[API] getMyGarden");
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
        log.info("[API] getPlaceBelongUser");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        List<PlaceDto.getPlaceBelongUser> belongList = placeService.getPlaceBelongUser(user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlaceBelongUser",belongList));
    }

    // 친구 리스트 조회
    @GetMapping("/{code}/friends")
    public ResponseEntity<JsonResponse> getPlaceFriends(@PathVariable String code){
        log.info("[API] getPlaceFriends");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        placeService.userOnPlace(user, code);
        List<PlaceDto.getPlaceFriends> userList = placeService.getPlaceFriends(code);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlaceFriends", userList));
    }

    // 장소 수정
    @PutMapping("/update/{code}")
    public ResponseEntity<JsonResponse> updatePlace(@PathVariable String code,
                                                    @RequestPart(value = "place") PlaceDto.updatePlaceReq req,
                                                    @RequestPart(value = "image") MultipartFile image){
        log.info("[API] updatePlace");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        PlaceDto.updatePlaceRes updatedPlace = placeService.updatePlace(user, code, req, image);
        return ResponseEntity.ok(new JsonResponse(true, 200, "updatePlace", updatedPlace));
    }

    // 장소 삭제
    // - 장소 탈퇴시
    //    - 팀원이 탈퇴시
    //        - 사용자가 등록한 메모, 캘린더 일정
    //            → 사용자 null 로 설정 후 장소에서 팀원 삭제
    //            → 사용자 id 를 어떻게 보여줄지 벤치마킹
    //    - 팀짱이 탈퇴시
    //        - 팀원이 등록된 순서대로 팀짱 넘겨주기!!
    //        - 장소 수정에서 팀장을 넘겨줄 수 있게!! (일단 디자인 반영!!)
    //        → 모든 사용자가 장소 탈퇴시 장소는 삭제
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<JsonResponse> deletePlace(@PathVariable String code){
        log.info("[API] deletePlace");
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        placeService.leavePlace(user, code);

        return ResponseEntity.ok(new JsonResponse(true, 200, "leavePlace", null));
    }

}
