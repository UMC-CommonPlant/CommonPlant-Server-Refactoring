package com.umc.commonplant.domain.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.commonplant.global.Jwt.JwtService;
import com.umc.commonplant.domain.oauth.dto.KakaoProfile;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String oAuthLogin(String accessToken, String provider){
        String email = "";
        email = kakaoLogin(accessToken);

        if(userRepository.countUserByEmail(email, provider) > 0){ // 로그인 성공
            User user = userRepository.findByEmail(email, provider);
            String token = jwtService.createToken(user.getUuid());
            return token;
        }
        else{
            return null;
        }
    }
    public String kakaoLogin(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response;
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        String redirect_uri="https://kapi.kakao.com/v2/user/me";

        KakaoProfile kakaoProfile = null;
        try{
            response=restTemplate.exchange(redirect_uri, HttpMethod.POST, request, String.class);
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        }catch(HttpClientErrorException e){
            log.info("[REJECT]kakao login error");

        }catch(JsonProcessingException e){
            log.info("[REJECT]kakaoMapper error");
        }
        return kakaoProfile.getKakao_account().getEmail();
    }
}
