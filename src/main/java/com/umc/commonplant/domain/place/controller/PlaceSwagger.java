package com.umc.commonplant.domain.place.controller;

import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface PlaceSwagger {

    @Operation(summary = "getPlace", description = "장소 코드로 장소 정보 조회")
    @Parameter(name = "code", description = "place code")
    public ResponseEntity<JsonResponse> getPlace(@PathVariable("code") String code);

}
