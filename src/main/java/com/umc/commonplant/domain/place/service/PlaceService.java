package com.umc.commonplant.domain.place.service;

import com.umc.commonplant.domain.belong.entity.Belong;
import com.umc.commonplant.domain.belong.entity.BelongRepository;
import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.place.entity.PlaceRepository;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.utils.openAPI.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.*;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final OpenApiService openApiService;
    private final PlaceRepository placeRepository;
    private final BelongRepository belongRepository;
    private final ImageService imageService;


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

    @Transactional
    public PlaceDto.getPlaceRes getPlace(User user, String code) {

        Place place = placeRepository.getPlaceByCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE));

        List<PlaceDto.getPlaceResUser> userList = belongRepository.getUserByPlaceCode(code).orElseThrow(() -> new BadRequestException(NOT_FOUND_PLACE_CODE))
                .stream().map(u -> new PlaceDto.getPlaceResUser(u.getName(), u.getImgUrl())).collect(Collectors.toList());


        PlaceDto.getPlaceRes res = PlaceDto.getPlaceRes.builder()
                .name(place.getName())
                .address(place.getAddress())
                .code(place.getCode())
                .isOwner(false)
                .humidity("")
                .maxTemp("")
                .minTemp("")
                .userList(userList)
                .build();

        if(place.getOwner().getUserIdx() == user.getUserIdx())
            res.setOwner(true);

        return res;
    }
}
