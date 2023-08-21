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
            infoRepository.save(info);
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }

//    public InfoDto.InfoResponse findInfo(String name) {
//
//        infoRepository.findByName(name);
//    }

}
