package com.umc.commonplant.domain2.Recommendation.service;

import com.umc.commonplant.domain2.Recommendation.entity.Recommendation;
import com.umc.commonplant.domain2.Recommendation.entity.RecommendationCategory;
import com.umc.commonplant.domain2.Recommendation.entity.RecommendationRepository;
import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.domain2.info.entity.Info;
import com.umc.commonplant.domain2.info.entity.InfoRepository;
import com.umc.commonplant.global.dto.JsonResponse;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final InfoRepository infoRepository;

    public void addRecommendationToInfo(Long infoId, String category) {
        RecommendationCategory recommendationCategory;
        try {
            recommendationCategory = RecommendationCategory.valueOf(category.toUpperCase());

        } catch (IllegalArgumentException e) {
            throw new GlobalException(ErrorResponseStatus.INVALID_CATEGORY_INFO);
        }

        Info info = infoRepository.findById(infoId)
                .orElseThrow(() -> new GlobalException(ErrorResponseStatus.NOT_EXIST_INFO));
        if(!info.getVerified()) {
            throw new GlobalException(ErrorResponseStatus.REJECT_INVALID_INFO);
        }

        Recommendation recommendation = Recommendation.builder()
                        .info(info).category(recommendationCategory).build();

        try {
            recommendationRepository.save(recommendation);
        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getMostSpecificCause();
            if (rootCause instanceof SQLIntegrityConstraintViolationException) {
                SQLIntegrityConstraintViolationException sqlException = (SQLIntegrityConstraintViolationException) rootCause;
                if (sqlException.getErrorCode() == 1062) {
                    throw new GlobalException(ErrorResponseStatus.ALREADY_EXIST_RECOMMEND);
                }
                else {
                    throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
                }
            }
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }
    }

    public List<InfoDto.SearchInfoResponse> getRecommendation(String category) {
        RecommendationCategory recommendationCategory;
        try {
            recommendationCategory = RecommendationCategory.valueOf(category.toUpperCase());

        } catch (IllegalArgumentException e) {
            throw new GlobalException(ErrorResponseStatus.INVALID_CATEGORY_INFO);
        }

        List<InfoDto.SearchInfoResponse> infoResponseList = new ArrayList<>();

        try {
            List<Recommendation> recommendationList = recommendationRepository.findByCategory(recommendationCategory);
            for(Recommendation recommendation : recommendationList) {
                Info getInfo = recommendation.getInfo();
                InfoDto.SearchInfoResponse infoResponse = InfoDto.SearchInfoResponse.builder()
                        .name(getInfo.getName())
                        .scientific_name(getInfo.getScientificName())
                        .imgUrl(getInfo.getImgUrl())
                        .build();

                infoResponseList.add(infoResponse);
            }

        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }

        return infoResponseList;
    }

    public void deleteRecommendation(Long infoIdx, String category) {
        RecommendationCategory recommendationCategory;
        try {
            recommendationCategory = RecommendationCategory.valueOf(category.toUpperCase());

        } catch (IllegalArgumentException e) {
            throw new GlobalException(ErrorResponseStatus.INVALID_CATEGORY_INFO);
        }

        //찾고


        //없으면 오류

        //있으면 지우기

    }
}
