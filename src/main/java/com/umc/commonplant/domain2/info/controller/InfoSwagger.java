package com.umc.commonplant.domain2.info.controller;

import com.umc.commonplant.domain2.info.dto.InfoDto;
import com.umc.commonplant.global.dto.JsonResponse;
import com.umc.commonplant.global.exception.ErrorResponse;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Information Controller", description = "식물도감 관련 API")
public interface InfoSwagger {

    @Operation(summary = "식물도감 정보 생성", description = "등록되어 있지 않은 식물을 사용자가 임시로 이름만 이용하여 등록한 뒤 사용할 수 있도록 함. 단, 이를 관리자가 수정(업데이트)하기 전까지는 식물 도감에서 보이지 않음")
    public ResponseEntity<JsonResponse> createInfo(
            @Parameter(
                    name = "infoRequest",
                    description = "InfoRequest 객체",
                    required = true
            ) @RequestPart("infoRequest") InfoDto.InfoRequest infoRequest,
            @Parameter(
                name = "image",
                description = "식물의 이미지"
            ) @RequestPart(value = "image", required = false) MultipartFile multipartFile
    );

    @Hidden
    public ResponseEntity<JsonResponse> updateInfo(@RequestPart("infoRequest") InfoDto.InfoRequest infoRequest, @RequestPart("image") MultipartFile multipartFile);

    @Operation(summary = "식물도감 정보 불러오기", description = "식물 이름(또는 학술명)의 정확한 이름을 통해 자세한 식물 정보를 가져옵니다.") //api 설명
    @Parameter(name = "name", description = "완벽한 식물 이름(또는 학술명)", example = "몬스테라 카스테니안", required = true)
    public ResponseEntity<JsonResponse> findInfo(@RequestParam("name") String name);


    @Operation(summary = "식물도감 검색", description = "식물 이름(또는 학술명)의 일부로 식물을 찾습니다.")
    @Parameter(name = "name", description = "식물 이름(또는 학술명)의 일부", example = "mons", required = true)
    public ResponseEntity<JsonResponse> searchInfo(@RequestParam("name") String name);
}
