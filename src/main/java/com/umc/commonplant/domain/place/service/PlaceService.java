package com.umc.commonplant.domain.place.service;

import com.umc.commonplant.domain.belong.entity.Belong;
import com.umc.commonplant.domain.belong.entity.BelongRepository;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.place.entity.PlaceRepository;
import com.umc.commonplant.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final BelongRepository belongRepository;


    public String create(User user, PlaceDto.create req, MultipartFile image) {
        String newCode = RandomStringUtils.random(6,33,125,true,false);

        //TODO : 카카오맵 API로 좌표 받아오기 구현

        Place place = Place.builder()
                .address(req.getAddress())
                .code(newCode)
                .gridX(null) //수정 필요
                .gridY(null) //수정 필요
                .imgUrl("https://firebasestorage.googleapis.com/v0/b/common-plant.appspot.com/o/commonPlant_plant/몬테123_HPJjiN?alt=media")  //수정 필요
                .user(user).build();
        placeRepository.save(place);

        Belong belong = Belong.builder().user(user).place(place).build();
        belongRepository.save(belong);
        return newCode;
    }
}
