package com.umc.commonplant.domain.Jwt;

import lombok.Getter;

import java.time.Duration;

@Getter
public class JwtSecret {
    public static String SECRET = "commontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplant";
    public static int EXPIRATION_TIME = 24*24*60*60*1000; // 24일 테스트용 (1/1000초)
}
