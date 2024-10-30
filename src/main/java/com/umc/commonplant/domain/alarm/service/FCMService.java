package com.umc.commonplant.domain.alarm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.umc.commonplant.domain.alarm.dto.FCMMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * FCMService 객체를 생성해 sendMessageTo()를 호출
 */
@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class FCMService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/commonplant-c6ae2/messages:send";
    private final ObjectMapper objectMapper;

    // TODO: TargetToken 연결 작업
    /**
     * 메세지 전송을 위한 접근 토큰 발급
     * @return String AccessToken -> RestAPI를 이용해 FCM에 Push 요청을 보낼 때 Header에 설정하여 인증을 위해 사용
     * @throws IOException
     */
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/serviceAccountKey.json";

        // GoogleCredentials: Google API를 사용하기 위해서 OAuth2를 이용해 인증한 대상을 나타내는 객체
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        // .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }

    /**
     * 메시지 생성
     * @param targetToken
     * @param title
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
    // private String makeMessage(String title, String body) throws JsonProcessingException {
        FCMMessageDto fcmMessage = FCMMessageDto.builder()
                .message(FCMMessageDto.Message.builder()
                        .token(targetToken)
                        .notification(FCMMessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .apns(FCMMessageDto.Apns.builder()
                                .payload(FCMMessageDto.Payload.builder()
                                        .aps(FCMMessageDto.Aps.builder().sound("default").build())
                                        .build())
                                .build())
                        .build())
                .validate_only(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    /**
     * 메시지 전송
     * @param targetToken: device 구분(FCM을 이용해 프론트를 구현할 때 얻어낼 수 있음)
     * @param title
     * @param body
     * @throws IOException
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
    // public void sendMessageTo(String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        // String message = makeMessage(title, body);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request)
                .execute();

        log.info(response.body().string());
    }

}
