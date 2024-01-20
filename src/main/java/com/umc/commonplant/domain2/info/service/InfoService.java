package com.umc.commonplant.domain2.info.service;

import com.umc.commonplant.domain2.history.service.HistoryService;
import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.domain2.info.entity.Info;
import com.umc.commonplant.domain2.info.entity.InfoRepository;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InfoService {

    private final InfoRepository infoRepository;
    private final ImageService imageService;
    private final HistoryService historyService;

    public void createInfo(InfoDto.InfoRequest infoRequest, MultipartFile multipartFile) {
        String imgUrl = null;
        Info info = infoRequest.toEntity();

        if(multipartFile != null && !multipartFile.isEmpty()) {
            imgUrl = imageService.saveImage(multipartFile);
            info.setImgUrl(imgUrl);
        }

        List<Info> exsitingInfoList = infoRepository.findByName(infoRequest.getName());
        if((!exsitingInfoList.isEmpty())) {
            if(!exsitingInfoList.get(0).getVerified()) {
                return ;
            }
            throw new GlobalException(ErrorResponseStatus.ALREADY_EXIST_INFO);
        }
        if(info.getVerified() == null) {
            info.setVerified(false);
        }

        String scientific_name = info.getScientificName();
        if(!scientific_name.isEmpty() && !scientific_name.isBlank()) {
            if(infoRepository.existsByScientificName(scientific_name)) {
                throw new GlobalException(ErrorResponseStatus.ALREADY_EXIST_INFO);
            }
        }

        try {
            infoRepository.save(info);
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }

    }

    public void updateInfo(InfoDto.InfoRequest infoRequest, MultipartFile multipartFile) {
        List<Info> existingInfoList = infoRepository.findByName(infoRequest.getName());
        if(existingInfoList.isEmpty()) {
            throw new GlobalException(ErrorResponseStatus.NOT_EXIST_INFO);
        }
        Info existingInfo = existingInfoList.get(0);

        Info info = infoRequest.toEntity();

        if(multipartFile != null && !multipartFile.isEmpty()) {
            String imgUrl = imageService.saveImage(multipartFile);
            info.setImgUrl(imgUrl);
        }
        info.setId(existingInfo.getInfoIdx());
        info.setVerified(true);

        try {
            infoRepository.save(info);
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }

    public InfoDto.InfoResponse findInfo(String name) {

        return infoRepository.findVerifiedByNameOrScientificName(name)
                .map(info -> {
                    historyService.searchInfo(info);
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
                            .imgUrl(info.getImgUrl())
                            .build();
                })
                .orElseThrow(() -> new GlobalException(ErrorResponseStatus.NOT_EXIST_INFO));
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

    public List<InfoDto.SearchInfoResponse> searchInfo(String name) {
        List<Info> infoList = infoRepository.findByNameOrScientificNameContainingAndVerified(name);

        return infoList.stream()
                .map(info -> InfoDto.SearchInfoResponse.builder()
                        .name(info.getName())
                        .scientific_name(info.getScientificName())
                        .imgUrl(info.getImgUrl())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Info> getOneInfo(String name) {
        List<Info> infoList = infoRepository.findByName(name);
        return infoList;
    }
}
