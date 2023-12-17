package com.umc.commonplant.domain.place.controller;

import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface PlaceSwagger {

    @ApiOperation(value = "장소 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "장소 코드", example = "XFPFAS"),
    })
    public ResponseEntity<JsonResponse> getPlace(@PathVariable("code") String code);

}
