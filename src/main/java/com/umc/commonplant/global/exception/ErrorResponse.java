package com.umc.commonplant.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final boolean isSuccess;
    private final int code;
    private final String message;

    public ErrorResponse(ErrorResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
