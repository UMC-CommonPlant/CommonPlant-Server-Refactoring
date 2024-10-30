package com.umc.commonplant.domain.calendar.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.calendar.dto.CalendarDto;
import com.umc.commonplant.domain.calendar.service.CalendarService;
import com.umc.commonplant.domain.memo.dto.MemoDto;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/calendar")
@RequiredArgsConstructor
@RestController
public class CalendarController implements CalendarSwagger {

    private final CalendarService calendarService;
    private final JwtService jwtService;
    private final UserService userService;

    @Operation(hidden = true)
    @GetMapping
    public ResponseEntity<JsonResponse> getCalendarByDate(@RequestParam("year") String year,
                                                          @RequestParam("month") String month,
                                                          @RequestParam("day") String day) {
        log.info("[API] getCalendarByMonth - 캘린더 조회 (일별)");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getCalendarByDate", null));
    }

    /**
     * [GET] 캘린더 조회 (월별)
     * @return 월별 캘린더 정보
     */
    @GetMapping("/monthly")
    public ResponseEntity<JsonResponse> getCalendarByMonth(@RequestParam("year") String year,
                                                           @RequestParam("month") String month) {
        log.info("[API] getCalendarByMonth - 캘린더 조회 (월별)");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        CalendarDto.getMyCalendarRes monthlyCalendar = calendarService.getMyCalendar(user, year, month);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getCalendarByMonth", monthlyCalendar));
    }

    @Operation(hidden = true)
    @GetMapping("/place")
    public ResponseEntity<JsonResponse> getCalendarByPlace() {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        List<CalendarDto.getMyCalendarPlaceListRes> placeList = calendarService.getPlaceNameListByUser(user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlaceList", placeList));
    }

    @Operation(hidden = true)
    @GetMapping("/place/plant")
    public ResponseEntity<JsonResponse> getCalendarByPlaceAndPlant(@RequestParam("code") String code) {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        List<CalendarDto.getMyCalendarPlantListRes> plantList = calendarService.getPlantListByPlace(user, code);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlantList", plantList));
    }

    @Operation(hidden = true)
    @GetMapping("/place/plant/memo")
    public ResponseEntity<JsonResponse> getCalendarByPlaceAndPlantAndMemo(@RequestParam("code") String code,
                                                                          @RequestParam("plant") Long plantIdx) {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        List<CalendarDto.getMyCalendarMemoRes> memoList = calendarService.getAllMemoByPlant(plantIdx);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getMemoList", memoList));
    }

}
