package com.umc.commonplant.domain2.history.controller;

import com.umc.commonplant.domain2.history.dto.HistoryDto;
import com.umc.commonplant.domain2.history.service.HistoryService;
import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/word")
@RequiredArgsConstructor
@RestController
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/getWordList")
    public ResponseEntity<JsonResponse> getWordList() {

        HistoryDto.HistoryResponse historyResponse= historyService.getWordList();

        return ResponseEntity.ok(new JsonResponse(true, 200, "getWordList", historyResponse));
    }
}
