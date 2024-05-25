package com.umc.commonplant.domain2.info.controller;

import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.domain2.info.service.InfoService;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequestMapping("/info")
@RequiredArgsConstructor
@RestController
public class InfoController implements InfoSwagger{
    private final InfoService infoService;

    @PostMapping(value = "/addPlantInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> createInfo(@RequestPart("infoRequest") InfoDto.InfoRequest infoRequest, @RequestPart(value = "image", required = false) MultipartFile multipartFile) {

        infoService.createInfo(infoRequest, multipartFile);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlantInfo", null));
    }

    @PatchMapping("/updatePlantInfo")
    public ResponseEntity<JsonResponse> updateInfo(@RequestPart("infoRequest") InfoDto.InfoRequest infoRequest, @RequestPart("image") MultipartFile multipartFile) {

        infoService.updateInfo(infoRequest, multipartFile);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updatePlantInfo", null));
    }

    @GetMapping("/getPlantInfo")
    public ResponseEntity<JsonResponse> findInfo(@RequestParam("name") String name) {

        InfoDto.InfoResponse infoResponse = infoService.findInfo(name);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlantInfo", infoResponse));
    }

    @GetMapping("/searchInfo")
    public ResponseEntity<JsonResponse> searchInfo(@RequestParam("name") String name) {

        List<InfoDto.SearchInfoResponse> infoList = infoService.searchInfo(name);

        return ResponseEntity.ok(new JsonResponse(true, 200, "searchInfo", infoList));
    }

    @GetMapping("/searchInfoPlus")
    public ResponseEntity<JsonResponse> searchPlusInfo(@RequestParam("name") String name) {

        List<InfoDto.SearchInfoWithWaterResponse> infoList = infoService.searchPlusInfo(name);

        return ResponseEntity.ok(new JsonResponse(true, 200, "searchInfo", infoList));
    }
}
