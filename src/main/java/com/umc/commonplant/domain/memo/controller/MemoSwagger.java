package com.umc.commonplant.domain.memo.controller;

import com.umc.commonplant.domain.memo.dto.MemoDto;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemoSwagger {
    @Operation(summary = "메모 작성", description = "메모 작성을 위해 내용을 입력한 뒤 호출하는 API")
    public ResponseEntity<JsonResponse> createMemo(
            @Parameter(
                    name = "memoRequest",
                    description = "memoRequest 객체",
                    required = true
            ) @RequestPart("memoRequest") MemoDto.MemoRequest memoRequest,
            @Parameter(
                    name = "image",
                    description = "메모의 이미지"
            ) @RequestPart(value = "image", required = false) MultipartFile multipartFile
    );

    @Operation(summary = "메모 수정", description = "기존의 메모 내용을 수정하는 API로, 이미지 수정의 경우 이미지 파일을 전달하면 업데이트, multipart 파일이 null이고 imgUrl 전달받으면(이때 전꺼랑 같으면) 유지, 아니라면 삭제")
    public ResponseEntity<JsonResponse> updateMemo(
            @Parameter(
                    name = "memoRequest",
                    description = "memoRequest 객체",
                    required = true
            ) @RequestPart("memoRequest")MemoDto.MemoUpdateRequest memoUpdateRequest,
            @Parameter(
                    name = "image",
                    description = "메모의 이미지"
            ) @RequestPart(value = "image", required = false) MultipartFile multipartFile
    );

    @Operation(summary = "메모 삭제", description = "하나의 특정 메모를 삭제하는 API로, 작성자만 삭제 가능")
    @Parameter(name = "memo_idx", description = "메모의 memo index", example = "4", required = true)
    public ResponseEntity<JsonResponse> deleteMemo(@RequestParam("memo_idx") Long memo_idx);

    @Operation(summary = "하나의 메모 조회", description = "memo_idx에 해당하는 하나의 메모 조회")
    @Parameter(name = "memo_idx", description = "메모의 memo index", example = "4", required = true)
    public ResponseEntity<JsonResponse> getOneMemo(@RequestParam("memo_idx") Long memo_idx);

    @Operation(summary = "메모 리스트 조회", description = "plant_idx에 해당하는 모든 메모를 최신 날짜 순서로 조회")
    @Parameter(name = "plant_idx", description = "식물의 plant index", example = "1", required = true)
    public ResponseEntity<JsonResponse> getPlantMemo(@RequestParam("plant_idx") Long plant_idx);

    @Hidden
    public ResponseEntity<JsonResponse> getRecentMemo(@RequestParam("plant_idx") Long plant_idx);
}
