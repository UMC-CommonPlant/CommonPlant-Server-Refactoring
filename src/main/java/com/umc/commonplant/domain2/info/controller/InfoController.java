package com.umc.commonplant.domain2.info.controller;

import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.domain2.info.service.InfoService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/info")
@RequiredArgsConstructor
@RestController
public class InfoController {
    private final InfoService infoService;

    @PostMapping("/addPlantInfo")
    public ResponseEntity<JsonResponse> createInfo(@RequestPart("infoRequest") InfoDto.InfoRequest infoRequest, @RequestPart("image") MultipartFile multipartFile) {

        infoService.createInfo(infoRequest, multipartFile);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlantInfo", null));
    }

//    @PostMapping("/getPlantInfo")
//    public ResponseEntity<JsonResponse> findInfo(@RequestPart("name") String name) {
//
//        InfoDto.InfoResponse infoResponse = infoService.findInfo(name);
//
//        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlantInfo", infoResponse));
//    }

}