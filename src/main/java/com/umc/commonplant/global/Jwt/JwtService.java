package com.umc.commonplant.global.Jwt;

import com.umc.commonplant.domain.user.repository.UserRepository;
import com.umc.commonplant.global.exception.BadRequestException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private String secretKey= JwtSecret.SECRET;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;


    //객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init(){
        System.out.println(secretKey);
        secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        System.out.println(secretKey);
    }

    public String createToken(String userUUID){
        Claims claims = Jwts.claims().setSubject(userUUID); // JWT payload 에 저장되는 정보단위
//        claims.put("role", "ROLE_USER"); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        //Access Token
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtSecret.EXPIRATION_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    //토큰에서 회원 정보 추출
    public String getUserPk(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        }catch (NullPointerException e){
            log.info(e.getMessage());
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
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
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
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
        }catch (Exception e) {
            return false;
        }
        return false;
    }
    // 토큰 기반으로 인증정보를 가져오는 메서드
    public Authentication getAuthentication(String token){
//        Claims claims = getClaims(token);
//        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),
//                "", authorities), token, authorities);
    }
    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }
    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}