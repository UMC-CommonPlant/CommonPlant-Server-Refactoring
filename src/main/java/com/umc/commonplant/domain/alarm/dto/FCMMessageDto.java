package com.umc.commonplant.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 이 클래스를 통해 생성된 객체는
 * Object Mapper에 의해 String으로 변환되어
 * Http Post 요청의 Request Body에 포함
 */
@Builder
@AllArgsConstructor
@Getter
public class FCMMessageDto {

    private boolean validate_only;
    private Message message;

    /**
     * notification: 모든 플랫폼에서 사용할 기본 알림 템플릿
     * token: 메시지를 보낼 등록 토큰(특정 디바이스에 메시지를 보내기 위해 사용)
     */
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
        private Data data;
    }

    /**
     * title: 알림의 제목
     * body: 알림의 텍스트
     * image: 알림의 이미지
     */
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

    /**
     * 프론트 단에서 처리할 데이터
     */
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private String name;
        private String description;
    }

}
