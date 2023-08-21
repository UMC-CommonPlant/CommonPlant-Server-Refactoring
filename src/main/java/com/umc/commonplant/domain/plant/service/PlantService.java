package com.umc.commonplant.domain.plant.service;

import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.place.entity.PlaceRepository;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain.plant.entity.PlantRepository;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlantService {

    private final PlaceRepository placeRepository;
    private final PlantRepository plantRepository;

    private final PlaceService placeService;
    private final ImageService imageService;

    /**
     * createPlant: 식물 추가
     * @param req
     * @param plantImage
     * @return
     */
    @Transactional
    public String createPlant(User user, PlantDto.createPlantReq req, MultipartFile plantImage) {

        // 식물 이미지
        String imgUrl = null;

        if (plantImage.getSize() > 0) {
            imgUrl = imageService.saveImage(plantImage);
        } else {
            throw new BadRequestException(ErrorResponseStatus.NO_SELECTED_PLANT_IMAGE);
        }

        // 식물 장소
        // TODO: 식물이 있는 장소 -> 장소의 코드로 검색(추후 장소의 이름으로 바뀔 수 있음)
        Place plantPlace = placeRepository.getPlaceByCode(req.getPlace())
                .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.NOT_FOUND_PLACE_NAME));

        // 최근에 물 준 날짜
        // TODO: WateredDate -> String에서 LocalDateTime으로 변환
        String strWateredDate = req.getStrWateredDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        LocalDateTime parsedWateredDate = null;
        parsedWateredDate = LocalDate.parse(strWateredDate, dateTimeFormatter).atStartOfDay();

        // TODO: 식물 종 이름 -> 장소 처럼 검색(현재는 임시로 Info 객체 생성)
        Plant plant = Plant.builder()
                .place(plantPlace)
                //.info(new Info())
                .name(req.getNickname())
                .imgUrl(imgUrl)
                .wateredDate(parsedWateredDate)
                .build();

        plantRepository.save(plant);

        return req.getNickname();
    }

    /**
     * getPlantList: 같은 장소에 있는 식물 리스트 조회
     * @param placeCode: 장소 코드
     * @return List<Plant>
     */
    @Transactional
    public List<Plant> getPlantList(String placeCode) {

        // TODO: 식물이 있는 장소 -> 장소의 코드로 검색(추후 장소의 이름으로 바뀔 수 있음)
        Place plantPlace = placeRepository.getPlaceByCode(placeCode)
                .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.NOT_FOUND_PLACE_NAME));

        return plantRepository.findAllByPlace(plantPlace);
    }

    /**
     * updatePlant: 식물 수정
     * @param plantIdx
     * @param nickname
     * @param plantImage
     * @return
     */
    @Transactional
    public String updatePlant(Long plantIdx, String nickname, MultipartFile plantImage) {

        Plant plant = plantRepository.findByPlantIdx(plantIdx)
                .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.NOT_FOUND_PLANT));

        // TODO: 식물의 애칭
        String plantNickname = null;

        if (nickname.isEmpty()) {
            throw new BadRequestException(ErrorResponseStatus.NO_PLANT_NICKNAME);
        } else if (nickname.length() <= 10) {
            plantNickname = nickname;
        } else {
            throw new BadRequestException(ErrorResponseStatus.LONG_PLANT_NICKNAME);
        }

        // TODO: imgUrl
        String imgUrl = null;

        if (plantImage.getSize() > 0) {
            imgUrl = plant.getImgUrl();
        } else {
            throw new BadRequestException(ErrorResponseStatus.NO_SELECTED_PLANT_IMAGE);
        }

        plant.updatePlant(imgUrl, plantNickname);

        return plantRepository.save(plant).getName();
    }
}
