package com.umc.commonplant.domain2.Recommendation.controller;

import com.umc.commonplant.domain2.Recommendation.entity.RecommendationCategory;
import com.umc.commonplant.domain2.Recommendation.service.RecommendationService;
import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.global.dto.JsonResponse;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/recommend")
@RequiredArgsConstructor
@RestController
public class RecommendationController {
    private final RecommendationService recommendationService;
    @PostMapping("/add")
    public ResponseEntity<JsonResponse> addRecommendation(@RequestParam("info_idx") Long info_idx,
                                                          @RequestParam("category") String category) {

        recommendationService.addRecommendationToInfo(info_idx, category);
        return ResponseEntity.ok(new JsonResponse(true, 200, "addRecommendation", null));
    }

    @GetMapping("/getRecommendInfos")
    public ResponseEntity<JsonResponse> addRecommendation(@RequestParam("category") String category) {
        List<InfoDto.SearchInfoResponse> infoResponseList = recommendationService.getRecommendation(category);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addRecommendation", infoResponseList));
    }
}
