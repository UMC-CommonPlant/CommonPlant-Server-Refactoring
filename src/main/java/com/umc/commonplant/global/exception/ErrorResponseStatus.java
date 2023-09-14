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
    NOT_FOUND_PLACE_NAME(false, 4101, "등록된 장소 이름이 없습니다."),
    OBJECT_MAPPER_FAIL(false, 4102,"Json 변환에 실패했습니다."),
    GET_HUMIDITY_FAIL(false, 4103,"습도를 조회하는데 실패했습니다."),
    NOT_FOUNT_USER_ON_PLACE(false, 4104,"place에 속하지 않은 유저입니다."),


    NOT_FOUND_PLANT(false, 4200, "등록된 식물이 없습니다."),
    NO_PLANT_NICKNAME(false, 4201, "식물의 애칭을 입력해 주세요!"),
    LONG_PLANT_NICKNAME(false, 4202, "식물의 애칭은 10자 이하로 설정해주세요!"),
    NO_SELECTED_PLANT_IMAGE(false,4203, "식물의 이미지를 선택해주세요!"),

    NO_SELECTED_STORY_IMAGE(false, 4300,"story의 이미지는 최소 1장, 최대 5장 입니다."),
    NOT_FOUND_STORY(false, 4200, "등록된 스토리가 없습니다."),

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