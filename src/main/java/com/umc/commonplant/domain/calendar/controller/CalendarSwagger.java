package com.umc.commonplant.domain.calendar.controller;

import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Calendar Controller", description = "캘린더 API")
public interface CalendarSwagger {

    /**
     * [GET] 캘린더 조회 (월별)
     * @return 월별 캘린더 정보
     */
    @Operation(summary = "캘린더 조회 (월별)", description = "원하는 달의 캘린더의 조회할 수 있고, 이벤트는 점으로 표시됨")
    public ResponseEntity<JsonResponse> getCalendarByMonth (
            @Parameter(
                    description = "캘린더의 연도 (String)",
                    example = "2024"
            )
            @RequestParam("year") String year,
            @Parameter(
                    description = "캘린더의 월 (String)",
                    example = "8"
            )
            @RequestParam("month") String month
    );

}
