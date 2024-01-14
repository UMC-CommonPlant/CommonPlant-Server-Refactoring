package com.umc.commonplant.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.oauth.OAuthInfoResponse;
import com.umc.commonplant.domain.user.dto.KakaoProfile;
import com.umc.commonplant.domain.user.dto.UserDto;
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
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public String oAuthLogin(String accessToken, String provider){
        String email = "";
        String name = "";
        String img = "";
        MultipartFile mockImg = null;
//        email = kakaoLogin(accessToken);
        KakaoProfile kakaoProfile = kakaoLogin(accessToken);
        email = kakaoProfile.getKakao_account().getEmail();
        name = kakaoProfile.getProperties().getNickname();
        img = kakaoProfile.getProperties().getProfile_image();

        if(userRepository.countUserByEmail(email, provider) > 0){
            User user = userRepository.findByEmail(email, provider);
            String token = jwtService.createToken(user.getUuid());
            return token;
        }else{
            //join
            UserDto.join req = new UserDto.join();
            req.setEmail(email);
            req.setName(name);
            req.setProvider("kakao");
            return userService.joinUser(req, mockImg);
        }
    }
    public KakaoProfile kakaoLogin(String accessToken){
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
        return kakaoProfile;
    }
}
