package com.umc.commonplant.domain2.info.service;

import com.umc.commonplant.domain.history.service.HistoryService;
import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.domain2.info.entity.Info;
import com.umc.commonplant.domain2.info.entity.InfoRepository;
import com.umc.commonplant.global.exception.ErrorResponse;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InfoService {

    private final InfoRepository infoRepository;
    private final ImageService imageService;
    private final HistoryService historyService;

    public void createInfo(InfoDto.InfoRequest infoRequest, MultipartFile multipartFile) {
        String imgUrl = null;
        Info info = infoRequest.toEntity();

        if(!(multipartFile.isEmpty())) {
            imgUrl = imageService.saveImage(multipartFile);
            info.setImgUrl(imgUrl);
        }

        List<Info> exsitingInfoList = infoRepository.findByName(infoRequest.getName());
        if((!exsitingInfoList.isEmpty())) {
            throw new GlobalException(ErrorResponseStatus.ALREADY_EXIST_INFO);
        }
        try {
            infoRepository.save(info);
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }

    }

    public void updateInfo(InfoDto.InfoRequest infoRequest, MultipartFile multipartFile) {
        String imgUrl = null;
        Info info = infoRequest.toEntity();

        if(!(multipartFile.isEmpty())) {
            imgUrl = imageService.saveImage(multipartFile);
            info.setImgUrl(imgUrl);
        }

        List<Info> exsitingInfoList = infoRepository.findByName(infoRequest.getName());
        if((!exsitingInfoList.isEmpty())) {
            Info exsitingInfo = exsitingInfoList.get(0);
            info.setId(exsitingInfo.getInfoIdx());

            try{
                infoRepository.save(info);
            } catch (Exception e) {
                throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
            }
        } else {
            throw new GlobalException(ErrorResponseStatus.NOT_EXIST_INFO);
        }
    }

    public InfoDto.InfoResponse findInfo(String name) {

        Optional<Info> infoOptional = infoRepository.findByNameOrScientificName(name, name);

        if (infoOptional.isPresent()) {
            Info info = infoOptional.get();
            historyService.searchInfo(name);
            String waterType = getWaterTypeByMonth(info);

            return InfoDto.InfoResponse.builder()
                    .name(info.getName())
                    .humidity(info.getHumidity())
                    .management(info.getManagement())
                    .place(info.getPlace())
                    .scientific_name(info.getScientificName())
                    .water_day(info.getWater_day())
                    .sunlight(info.getSunlight())
                    .temp_max(info.getTemp_max())
                    .temp_min(info.getTemp_min())
                    .tip(info.getTip())
                    .water_type(waterType)
                    .build();
        } else {
            throw new GlobalException(ErrorResponseStatus.NOT_EXIST_INFO);
        }
    }

    private String getWaterTypeByMonth(Info info) {
        Month currentMonth = LocalDate.now().getMonth();

        if (currentMonth == Month.MARCH || currentMonth == Month.APRIL || currentMonth == Month.MAY) {
            return info.getWater_spring();
        } else if (currentMonth == Month.JUNE || currentMonth == Month.JULY || currentMonth == Month.AUGUST) {
            return info.getWater_summer();
        } else if (currentMonth == Month.SEPTEMBER || currentMonth == Month.OCTOBER || currentMonth == Month.NOVEMBER) {
            return info.getWater_autumn();
        } else {
            return info.getWater_winter();
        }
    }


    //식물 리스트 조회 (단어 포함되는 식물의 이름, url, 학술명)
    //단, 이름의 일부 / 학술명의 일부 둘다 가능해야 함
    //이건 사용자의 입력이 존재하므로 특수문자 처리 해줘야함
//    public List<InfoDto.SearchInfoResponse> searchInfo(String name) {
//
//
//
//    }

    //추천식물 리스트 조회? (테이블 따로였나

}
