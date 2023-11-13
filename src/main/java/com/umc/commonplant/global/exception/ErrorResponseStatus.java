package com.umc.commonplant.global.exception;

public enum ErrorResponseStatus {
    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    FAILED_TO_LOGIN_JWT(false,2004,"token을 확인하세요."),

    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // 4000 : User
    EXIST_USER(false, 4002, "이미 등록된 유저가 있습니다."),
    NOT_FOUND_USER(false, 4003, "등록된 유저가 없습니다."),
    EXPIRED_JWT(false, 4007, "만료된 토큰입니다."),

    // 4100 : Place
    NOT_FOUND_PLACE_CODE(false, 4100, "등록된 code가 없습니다."),
    NOT_FOUND_PLACE_NAME(false, 4101, "등록된 장소 이름이 없습니다."),
    GET_HUMIDITY_FAIL(false, 4103,"습도를 조회하는데 실패했습니다."),
    NOT_FOUNT_USER_ON_PLACE(false, 4104,"place에 속하지 않은 유저입니다."),

    // 4200 : Plant
    NOT_FOUND_PLANT(false, 4200, "등록된 식물이 없습니다."),
    NO_PLANT_NICKNAME(false, 4201, "식물의 애칭을 입력해 주세요!"),
    LONG_PLANT_NICKNAME(false, 4202, "식물의 애칭은 10자 이하로 설정해주세요!"),
    NO_SELECTED_PLANT_IMAGE(false,4203, "식물의 이미지를 선택해주세요!"),


    // 4300 : Memo

    // 4400 : Image
    NO_SELECT_IMAGE(false, 4400, "이미지가 선택되지 않았습니다."),
    INVALID_IMAGE_URL(false, 4402, "유효하지 않은 이미지 URL 입니다."),
    IMAGE_DELETE_FAIL(false, 4403, "이미지 삭제에 실패했습니다."),
    IMAGE_UPLOAD_FAIL(false, 4404, "이미지 업로드에 실패했습니다."),

    // 4410 : Information
    ALREADY_EXIST_INFO(false, 4410, "이미 존재하는 식물 정보입니다."),
    NOT_EXIST_INFO(false, 4411, "존재하지 않는 식물 정보입니다."),
    INVALID_CATEGORY_INFO(false, 4412, "잘못된 카테고리 이름입니다"),
    REJECT_INVALID_INFO(false, 4413, "검증되지 않은 식물 정보는 등록될 수 없습니다"),
    ALREADY_EXIST_RECOMMEND(false, 4414, "이미 존재하는 조합입니다"),

    // 4500 : QnA

    // 4600 : Calendar

    // 4700 : Alarm

    // 4800 : Story
    NO_SELECTED_STORY_IMAGE(false, 4300,"story의 이미지는 최소 1장, 최대 5장 입니다."),
    NOT_FOUND_STORY(false, 4301, "등록된 스토리가 없습니다."),
    NOT_FOUNT_PARENT_COMMENT(false, 4401, "등록된 댓글이 없습니다."),

    //5000 : Server connection 오류
    SERVER_ERROR(false, 5000, "서버와의 연결에 실패하였습니다."),
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    OBJECT_MAPPER_FAIL(false, 4102,"Json 변환에 실패했습니다."),

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