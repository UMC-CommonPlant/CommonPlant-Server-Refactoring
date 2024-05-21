package com.umc.commonplant.domain.alarm.controller;

import com.umc.commonplant.domain.alarm.dto.FCMMessageTestDto;
import com.umc.commonplant.domain.alarm.service.FCMService;
import com.umc.commonplant.global.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final FCMService fcmService;

    @PostMapping("/api/fcm")
    public ResponseEntity<ErrorResponse> pushMessage(@RequestBody FCMMessageTestDto fcmTestDto) throws IOException {

        log.info(fcmTestDto.getTargetToken() + " " +fcmTestDto.getTitle() + " " + fcmTestDto.getBody());
        // log.info(fcmTestDto.getTitle() + " " + fcmTestDto.getBody());

        fcmService.sendMessageTo(
                fcmTestDto.getTargetToken(),
                fcmTestDto.getTitle(),
                fcmTestDto.getBody()
        );

        return ResponseEntity.ok().build();
    }

}
