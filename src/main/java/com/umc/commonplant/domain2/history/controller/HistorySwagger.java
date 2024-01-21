package com.umc.commonplant.domain2.history.controller;

import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "History Controller", description = "인기 검색어를 위한 API")
public interface HistorySwagger {
    @Operation(summary = "인기 검색어 불러오기", description = "현재 시점으로 인기 검색어를 불러오는 API")
    public ResponseEntity<JsonResponse> getWordList();
}
