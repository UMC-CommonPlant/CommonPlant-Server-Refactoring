package com.umc.commonplant.domain2.Recommendation.controller;


import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Recommendation Controller", description = "추천 관련 API")
public interface RecommendationSwagger {
    @Hidden
    public ResponseEntity<JsonResponse> addRecommendation(@RequestParam("info_idx") Long info_idx,
                                                          @RequestParam("category") String category);
    @Operation(summary = "추천식물 리스트 조회", description = "카테고리를 입력하면 해당하는 식물 리스트 반환.</br>STUDIO(원룸), AIR_PURIFYING(공기정화), BEGINNER_FRIENDLY(초보집사), SHADE_LOVING(어두운 곳을 좋아함), LIGHT_LOVING(밝은 곳을 좋아함), WATER_LOVING(물을 좋아함), DROUGHT_RESISTANT(물을 좋아하지 않음), INTERIOR(인테리어)")
    @Parameter(name = "category", description = "식물의 카테고리", example = "STUDIO", required = true)
    public ResponseEntity<JsonResponse> addRecommendation(@RequestParam("category") String category);

    @Hidden
    public ResponseEntity<JsonResponse> deleteRecommendation(@RequestParam("info_idx") Long info_idx,
                                                             @RequestParam("category") String category);
}
