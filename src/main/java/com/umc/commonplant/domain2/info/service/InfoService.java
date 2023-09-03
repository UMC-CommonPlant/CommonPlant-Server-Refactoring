package com.umc.commonplant.domain2.info.service;

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
import java.util.List;

@RequiredArgsConstructor
@Service
public class InfoService {

    private final InfoRepository infoRepository;
    private final ImageService imageService;

    public void createInfo(InfoDto.InfoRequest infoRequest, MultipartFile multipartFile) {
        String imgUrl = null;
        Info info = infoRequest.toEntity();

        if(!(multipartFile.isEmpty())) {
            imgUrl = imageService.saveImage(multipartFile);
            info.setImgUrl(imgUrl);
        }

        try {
            List<Info> exsitingInfoList = infoRepository.findByName(infoRequest.getName());
            if((!exsitingInfoList.isEmpty())) {
                Info exsitingInfo = exsitingInfoList.get(0);

                infoRepository.save(exsitingInfo);
            } else {
                infoRepository.save(info);
            }
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }

    public InfoDto.InfoResponse findInfo(String name) {

        Info info = infoRepository.findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new GlobalException(ErrorResponseStatus.RESPONSE_ERROR));

            String waterType = getWaterTypeByMonth(info);

        return InfoDto.InfoResponse.builder()
                .name(info.getName())
                .humidity(info.getHumidity())
                .management(info.getManagement())
                .place(info.getPlace())
                .scientific_name(info.getScientific_name())
                .water_day(info.getWater_day())
                .sunlight(info.getSunlight())
                .temp_max(info.getTemp_max())
                .temp_min(info.getTemp_min())
                .tip(info.getTip())
                .water_type(waterType)
                .build();
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

}
