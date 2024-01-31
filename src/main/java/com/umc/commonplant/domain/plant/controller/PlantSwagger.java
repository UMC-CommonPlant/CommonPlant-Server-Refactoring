package com.umc.commonplant.domain.plant.controller;

import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Plant Controller", description = "식물 관련 API")
public interface PlantSwagger {

    /**
     * 식물 추가
     * @return 추가한 식물의 애칭
     */
    @Operation(summary = "식물 추가", description = "장소를 선택한 후 식물을 생성함. 단, 장소에 속한 사람만 식물을 생성할 수 있음")
    public ResponseEntity<JsonResponse> createPlant (
            @Parameter(
                    name = "plant",
                    description = "식물 생성 객체 (createPlantReq)",
                    required = true
            ) @RequestPart("plant") PlantDto.createPlantReq createPlantReq,
            @Parameter(
                    name = "image",
                    description = "식물의 이미지",
                    required = true
            ) @RequestPart(value = "image") MultipartFile file
    );

    /**
     * 식물 조회
     * @return 식물 객체
     */
    @Operation(summary = "식물 1개 상세 조회", description = "D-Day를 포함한 식물의 상세 정보를 확인할 수 있음")
    public ResponseEntity<JsonResponse> getPlantCard (
            @Parameter(
                    description = "조회하는 식물의 인덱스 값"
            )
            @PathVariable Long plantIdx
    );

    /**
     * 식물의 D-Day 업데이트
     * @return
     */
    @Operation(summary = "식물의 D-Day 업데이트", description = "식물의 물주기 날짜를 업데이트할 수 있음")
    public ResponseEntity<JsonResponse> updateWateredDate (
            @Parameter(
                    description = "물주기를 업데이트 하는 식물의 인덱스 값"
            )
            @PathVariable Long plantIdx
    );

    /**
     * 식물을 수정할 때 뜨는 조회 화면
     * @return 수정하기 전 식물의 애칭
     */
    @Operation(summary = "식물을 수정할 때 뜨는 조회 화면", description = "자세한 설명은 API SHEET '식물 조회(식물 수정 화면)'에 적힌 화면을 참고해주세요!")
    public ResponseEntity<JsonResponse> getUpdatedPlant (
            @Parameter(description = "수정할 식물의 인덱스 값")
            @PathVariable Long plantIdx
    );


    /**
     * 식물 수정
     * @return 수정한 식물의 애칭
     */
    @Operation(summary = "식물 수정", description = "식물의 닉네임과 이미지만 수정 가능하며, 장소에 속한 사람만 식물을 수정할 수 있음")
    public ResponseEntity<JsonResponse> updatePlant (
            @Parameter(
                    description = "수정하는 식물의 인덱스 값"
            ) @PathVariable Long plantIdx,
            @Parameter(
                    name = "nickname",
                    description = "식물의 새로운 닉네임",
                    required = true
            ) @RequestPart("plant") PlantDto.updatePlantReq updatePlantReq,
            @Parameter(
                    name = "image",
                    description = "식물의 새로운 이미지",
                    required = true
            ) @RequestPart(value = "image") MultipartFile file
    );

    /**
     * 식물 삭제
     * @return
     */
    @Operation(summary = "식물 삭제", description = "장소에 속한 사람만 식물을 삭제할 수 있음")
    public ResponseEntity<JsonResponse> deletePlant (
            @Parameter(description = "삭제할 식물의 인덱스 값")
            @PathVariable Long plantIdx
    );

}
