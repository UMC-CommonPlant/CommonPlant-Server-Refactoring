package com.umc.commonplant.domain.Jwt;

import com.umc.commonplant.domain.user.repository.UserRepository;
import com.umc.commonplant.global.exception.BadRequestException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.EXPIRED_JWT;
import static com.umc.commonplant.global.exception.ErrorResponseStatus.FAILED_TO_LOGIN_JWT;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private String secretKey= JwtSecret.SECRET;
    private final UserRepository userRepository;


    //객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init(){
        System.out.println(secretKey);
        secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        System.out.println(secretKey);
    }

    public String createToken(String userUUID){
        Claims claims = Jwts.claims().setSubject(userUUID); // JWT payload 에 저장되는 정보단위
//        claims.put("roles", "ROLE_USER"); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        //Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtSecret.EXPIRATION_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return accessToken;
    }

    //토큰에서 회원 정보 추출
    public String getUserPk(String token){
        try{
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        }catch (NullPointerException e){
            log.info(e.getMessage());
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        }catch(ExpiredJwtException e){
            log.error("getUserPK : "+e.getMessage());
            return "getUserPK : Expired Token";
        }
    }
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-AUTH-TOKEN");
        /* return request.getHeader("AUTHORIZATION_HEADER");*/
    }

    //header에서 ACCESSTOKEN추출
    public String resolveToken(){
        String token = getJwt();
        validateToken(token);
        String uuid = getUserPk(token);
        userRepository.findByUuid(uuid).orElseThrow(()->new BadRequestException(FAILED_TO_LOGIN_JWT));
        return uuid;
    }

    //토큰의 유효성 + 만료 일자 확인
    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token. at validation Token");
            throw new BadRequestException(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new BadRequestException(FAILED_TO_LOGIN_JWT);
        }catch (Exception e) {
            return false;
        }
        return false;
    }


}