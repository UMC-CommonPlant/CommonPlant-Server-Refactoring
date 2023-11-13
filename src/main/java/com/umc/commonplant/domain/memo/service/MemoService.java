package com.umc.commonplant.domain.memo.service;

import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain.memo.dto.MemoDto;
import com.umc.commonplant.domain.memo.entity.Memo;
import com.umc.commonplant.domain.memo.entity.MemoRepository;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain.plant.entity.PlantRepository;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemoService {
    private final MemoRepository memoRepository;
    private final ImageService imageService;
    private final PlantRepository plantRepository;


    public void createMemo(User user, MemoDto.MemoRequest memoRequest, MultipartFile multipartFile) {
        if (memoRequest == null) throw new GlobalException(ErrorResponseStatus.EMPTY_INPUT_MEMO);
        if (memoRequest.getContent() == null) throw new GlobalException(ErrorResponseStatus.EMPTY_CONTENT_MEMO);
        if (memoRequest.getContent().length() > 200) throw new GlobalException(ErrorResponseStatus.OVERFLOW_CONTENT_MEMO);

        String imgUrl = null;
        if(multipartFile != null && !multipartFile.isEmpty()) {
            imgUrl = imageService.saveImage(multipartFile);
        }

        Plant plant = plantRepository.findByPlantIdx(memoRequest.getPlant_idx())
                .orElseThrow(() -> new GlobalException(ErrorResponseStatus.PLANT_NOT_FOUND));

        Memo memo = Memo.builder()
                .user(user)
                .plant(plant)
                .imgUrl(imgUrl)
                .content(memoRequest.getContent())
                .build();

        try {
            memoRepository.save(memo);
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }
}
