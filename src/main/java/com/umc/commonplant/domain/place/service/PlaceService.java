package com.umc.commonplant.domain.place.service;

import com.umc.commonplant.domain.belong.entity.Belong;
import com.umc.commonplant.domain.belong.entity.BelongRepository;
import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain.memo.entity.Memo;
import com.umc.commonplant.domain.memo.entity.MemoRepository;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.place.entity.PlaceRepository;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain.plant.entity.PlantRepository;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.repository.UserRepository;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.utils.UuidUtil;
import com.umc.commonplant.global.utils.openAPI.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.*;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final BelongRepository belongRepository;
    private final UserRepository userRepository;

    private final OpenApiService openApiService;
    private final ImageService imageService;
    private final PlantRepository plantRepository;
    private final MemoRepository memoRepository;


    @Transactional
    public String create(User user, PlaceDto.createPlaceReq req, MultipartFile image) {
        String newCode = RandomStringUtils.random(6,33,125,true,false);

        HashMap<String, String> gridXY = openApiService.getGridXYFromAddress(req.getAddress());

        String imgUrl = imageService.saveImage(image);

        Place place = Place.builder()
                .name(req.getName())
                .address(req.getAddress())
                .code(newCode)
                .gridX(gridXY.get("x"))
                .gridY(gridXY.get("y"))
                .imgUrl(imgUrl)
                .owner(user).build();
        placeRepository.save(place);

        Belong belong = Belong.builder().user(user).place(place).build();
        belongRepository.save(belong);
        return newCode;
    }

    public void userOnPlace(User user, String code)
    {
        if(belongRepository.countUserOnPlace(user.getUuid(), code) < 1)
            throw new BadRequestException(NOT_FOUND_USER_ON_PLACE);
    }

    @Transactional
    public PlaceDto.getPlaceRes getPlace(User user, String code) 
    {
        belongUserOnPlace(user, code);
        Place place = getPlaceByCode(code);
        List<PlaceDto.getPlaceResUser> userList = belongRepository.getUserListByPlaceCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE))
                .stream().map(u -> new PlaceDto.getPlaceResUser(u.getName(), u.getImgUrl())).collect(Collectors.toList());

        System.out.println("placeDto");
        PlaceDto.getPlaceRes res = PlaceDto.getPlaceRes.builder()
                .name(place.getName())
                .address(place.getAddress())
                .code(place.getCode())
                .isOwner(false)
                .userList(userList)
                .build();

        if(place.getOwner().getUserIdx() == user.getUserIdx())
            res.setOwner(true);

        return res;
    }
    @Transactional
    public List<PlaceDto.getPlaceListRes> getPlaceList(User user){
        //장소이미지, 장소이름, 참여인원수, 등록된 식물 수 보여주기 List
        List<Place> places = placeRepository.findAllByOwner(user);
        List<PlaceDto.getPlaceListRes> placeList = new ArrayList<>();

        for(Place place : places){
            String member = belongRepository.countUserByPlace(place);
            String plant = plantRepository.countPlantsByPlace(place);
            placeList.add(0, new PlaceDto.getPlaceListRes(place, member, plant));
        }

        return placeList;
    }

    public PlaceDto.getPlaceGridRes getPlaceGrid(User user, String code) {
        belongUserOnPlace(user, code);
        Place place = placeRepository.getPlaceByCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE));
        return new PlaceDto.getPlaceGridRes(place.getGridX(), place.getGridY());
    }
    public String newFriend(String name, String code){
        User newUser = userRepository.findByname(name).orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));
        Place place = getPlaceByCode(code);

        List<PlaceDto.getPlaceResUser> userList = belongRepository.getUserListByPlaceCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE))
                .stream().map(u -> new PlaceDto.getPlaceResUser(u.getName(), u.getImgUrl())).collect(Collectors.toList());

        // 장소에 이미 유저가 있을 경우 Exception
        long userCount = userList.stream().filter(n -> n.getName().equals(name)).count();
        if(userCount > 0){
            throw new BadRequestException(IS_USER_ON_PLACE);
        }
        // 장소에 유저가 없는 경우
        //belongUserOnPlace(newUser, code);

        Belong belong = Belong.builder().user(newUser).place(place).build();
        belongRepository.save(belong);

        return place.getCode();
    }

//    public Optional<User> getFriends(String inputName) {
//        Optional<User> users = Optional.ofNullable(userRepository.findByname(inputName).orElseThrow(() -> new BadRequestException(NOT_FOUND_USER)));
//
//        return users;
//    }

    public List<PlaceDto.getPlaceBelongUser> getPlaceBelongUser(User user) {
        List<Belong> belongs = belongRepository.getPlaceBelongUser(user.getUuid());
        List<PlaceDto.getPlaceBelongUser> belongList = new ArrayList<>();
        for(Belong b : belongs){
            PlaceDto.getPlaceBelongUser belongUser = new PlaceDto.getPlaceBelongUser(
                    b.getPlace().getCode(),
                    b.getPlace().getName(),
                    b.getPlace().getImgUrl());
            belongList.add(belongUser);
        }
        return belongList;
    }

    public List<PlaceDto.getPlaceFriends> getPlaceFriends(String code) {
        Place place = placeRepository.getPlaceByCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE));
        List<User> userList = belongRepository.getUserListByPlaceCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE));

        List<PlaceDto.getPlaceFriends> friends = new ArrayList<>();
        for(User u : userList){
            PlaceDto.getPlaceFriends user = new PlaceDto.getPlaceFriends(u.getImgUrl(), u.getName());
            if(place.getOwner().getUuid().equals(u.getUuid()))
                user.setLeader(true);
            friends.add(user);
        }
        return friends;
    }

    @Transactional
    public PlaceDto.updatePlaceRes updatePlace(User user, String code, PlaceDto.updatePlaceReq req, MultipartFile image) {
        Place place = getPlaceByCode(code);
        belongUserOnPlace(user, code);

        HashMap<String, String> gridXY = openApiService.getGridXYFromAddress(req.getAddress());

        String imgUrl = imageService.saveImage(image);

        Place newPlaceInfo = Place.builder()
                .name(req.getName())
                .address(req.getAddress())
                .code(code)
                .gridX(gridXY.get("x"))
                .gridY(gridXY.get("y"))
                .imgUrl(imgUrl)
                .owner(place.getOwner())
                .build();
        newPlaceInfo.setPlaceIdx(place.getPlaceIdx());
        placeRepository.save(newPlaceInfo);

        return new PlaceDto.updatePlaceRes(
                newPlaceInfo.getCode(),
                newPlaceInfo.getName(),
                newPlaceInfo.getAddress(),
                newPlaceInfo.getImgUrl()
        );
    }

    @Transactional
    public void leavePlace(User user, String code) {
        Place place = getPlaceByCode(code);

        Optional<Belong> belongInfo = belongRepository.getBelongByUserAndPlace(user.getUuid(), place.getCode());
        if(belongInfo.isEmpty()) throw new BadRequestException(NOT_FOUND_USER_ON_PLACE);
        Belong belong = belongInfo.get();

        List<Long> plants = plantRepository.findAllByPlace(place).stream()
                .map(Plant::getPlantIdx)
                .collect(Collectors.toList());

        User unknownUser = createUnknownUser();

        for(Long plantIdx : plants) {
            List<Memo> memos = memoRepository.findByPlantIdx(plantIdx);

            for(Memo memo : memos) {
                if(memo.getUser().equals(user)) {
                    Memo unknownMemo = Memo.builder()
                            .memoIdx(memo.getMemoIdx())
                            .user(unknownUser)
                            .plant(memo.getPlant())
                            .imgUrl(memo.getImgUrl())
                            .content(memo.getContent())
                            .build();
                    memoRepository.save(unknownMemo);
                }
            }
        }

        belongRepository.delete(belong);

        if(belongRepository.getNumberOfUserInPlace(code) == 0) {
            // TODO: 장소 완전 삭제
        } else {
            changeOwnerOfPlace(user, place.getCode());
        }

    }

    // ----- API 외 메서드 -----

    // 장소에 속한 유저인지 확인하는 메서드
    public void belongUserOnPlace(User user, String code)
    {
        if(belongRepository.countUserOnPlace(user.getUuid(), code) < 1)
            throw new BadRequestException(NOT_FOUND_USER_ON_PLACE);
    }

    // code를 입력하여 Place 정보 반환
    public Place getPlaceByCode(String code){
        return placeRepository.getPlaceByCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE));
    }

    // 유저가 속한 장소 리스트 반환
    public List<Place> getPlaceListByUser(User user){
        return belongRepository.getPlaceListByUser(user.getUuid());
    }

    // 장소에서 탈퇴한 팀원을 '알 수 없음'으로 변환
    public User createUnknownUser() {
        Optional<User> user = userRepository.findByname("알 수 없음");

        if(!userRepository.existsByname("알 수 없음")) {
            String uuid = UuidUtil.generateType1UUID();
            User unknownUser = new User("알 수 없음", "unknown@unknown.com", null, null, null, uuid);

            userRepository.save(unknownUser);

            return unknownUser;
        } else {
            return user.get();
        }
    }

    // 팀짱 넘겨주기
    public void changeOwnerOfPlace(User user, String code) {
        Place place = getPlaceByCode(code);

        if(place.getOwner().equals(user)) {
            List<User> users = belongRepository.getUserListByPlaceCodeOrderByCreatedAt(place.getCode())
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE));;
            place.setOwner(users.get(0));

            placeRepository.save(place);
        }
    }

}
