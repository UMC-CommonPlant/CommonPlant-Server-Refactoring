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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public void updateMemo(User user, MemoDto.MemoUpdateRequest memoUpdateRequest, MultipartFile multipartFile) {
        if (memoUpdateRequest == null) throw new GlobalException(ErrorResponseStatus.EMPTY_INPUT_MEMO);
        if (memoUpdateRequest.getContent() == null) throw new GlobalException(ErrorResponseStatus.EMPTY_CONTENT_MEMO);
        if (memoUpdateRequest.getContent().length() > 200) throw new GlobalException(ErrorResponseStatus.OVERFLOW_CONTENT_MEMO);

        Optional<Memo> existingMemoOptional = memoRepository.findById(memoUpdateRequest.getMemo_idx());
        if(existingMemoOptional.isEmpty()) throw new GlobalException(ErrorResponseStatus.NOT_EXIST_MEMO);
        Memo existingMemo = existingMemoOptional.get();

        if(!Objects.equals(existingMemo.getUser().getUserIdx(), user.getUserIdx())) {
            throw new GlobalException(ErrorResponseStatus.UNAUTHORIZED_USER_MEMO);
        }

        String imgUrl = null;
        if(multipartFile != null && !multipartFile.isEmpty()) {
            imgUrl = imageService.saveImage(multipartFile);
        } else {
            if(Objects.equals(existingMemo.getImgUrl(), memoUpdateRequest.getImgUrl())) {
                imgUrl = memoUpdateRequest.getImgUrl();
            }
        }

        Memo memo = Memo.builder()
                .memoIdx(memoUpdateRequest.getMemo_idx())
                .user(user)
                .plant(existingMemo.getPlant())
                .imgUrl(imgUrl)
                .content(memoUpdateRequest.getContent())
                .build();

        try {
            memoRepository.save(memo);
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }

    public void deleteMemo(User user, Long memoIdx) {
        Optional<Memo> existingMemoOptional = memoRepository.findById(memoIdx);
        if(existingMemoOptional.isEmpty()) throw new GlobalException(ErrorResponseStatus.NOT_EXIST_MEMO);
        Memo existingMemo = existingMemoOptional.get();

        if(!Objects.equals(existingMemo.getUser().getUserIdx(), user.getUserIdx())) {
            throw new GlobalException(ErrorResponseStatus.UNAUTHORIZED_DELETE_MEMO);
        }

        try {
            memoRepository.deleteById(memoIdx);
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }


    public MemoDto.GetOneMemo getOneMemo(Long memoIdx) {
        Optional<Memo> existingMemoOptional = memoRepository.findById(memoIdx);
        if(existingMemoOptional.isEmpty()) throw new GlobalException(ErrorResponseStatus.NOT_EXIST_MEMO);
        Memo existingMemo = existingMemoOptional.get();

        return MemoDto.GetOneMemo.builder()
                .memo_idx(existingMemo.getMemoIdx())
                .plant_idx(existingMemo.getPlant().getPlantIdx())
                .content(existingMemo.getContent())
                .imgUrl(existingMemo.getImgUrl())
                .build();
    }

    public List<MemoDto.GetAllMemo> getAllMemoByPlant(Long plantIdx) {
        List<Memo> existingMemo = memoRepository.findByPlantIdx(plantIdx);
        List<Memo> sortedMemos = existingMemo.stream()
                .sorted(Comparator.comparing(Memo::getCreatedAt).reversed())
                .collect(Collectors.toList());

        List<MemoDto.GetAllMemo> memoResponseList = new ArrayList<>();

        for(Memo memo : sortedMemos) {
            MemoDto.GetAllMemo getAllMemo = MemoDto.GetAllMemo.builder()
                    .memo_idx(memo.getMemoIdx())
                    .content(memo.getContent())
                    .imgUrl(memo.getImgUrl())
                    .writer(memo.getUser().getName())
                    .created_at(memo.getCreatedAt())
                    .build();

            memoResponseList.add(getAllMemo);
        }

        return memoResponseList;
    }

    public MemoDto.GetAllMemo getRecentMemoByPlant(Long plantIdx) {
        try {
            Pageable limit = PageRequest.of(0, 1);
            List<Memo> memos = memoRepository.findLatestMemoByPlantIdx(plantIdx, limit);
            Memo memo = memos.isEmpty() ? null : memos.get(0);

            assert memo != null;

            return MemoDto.GetAllMemo.builder()
                    .memo_idx(memo.getMemoIdx())
                    .content(memo.getContent())
                    .imgUrl(memo.getImgUrl())
                    .writer(memo.getUser().getName())
                    .created_at(memo.getCreatedAt())
                    .build();
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }
}
