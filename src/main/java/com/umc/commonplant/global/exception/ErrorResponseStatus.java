package com.umc.commonplant.global.exception;

public enum ErrorResponseStatus {
    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    FAILED_TO_LOGIN_JWT(false,2004,"token을 확인하세요."),


    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),


    //4000 : Database, Server 오류
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    EXIST_USER(false, 4002, "이미 등록된 유저가 있습니다."),
    NOT_FOUND_USER(false, 4003, "등록된 유저가 없습니다."),
    EXPIRED_JWT(false, 4007, "만료된 토큰입니다."),

    NOT_FOUND_PLACE_CODE(false, 4100, "등록된 code가 없습니다."),

    //5000 : Server connection 오류
    SERVER_ERROR(false, 5000, "서버와의 연결에 실패하였습니다."),

    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private ErrorResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}