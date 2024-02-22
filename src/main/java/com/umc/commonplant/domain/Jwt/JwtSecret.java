package com.umc.commonplant.domain.Jwt;

import lombok.Getter;

@Getter
public class JwtSecret {
    public static String SECRET = "commontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplant";
    public static int EXPIRATION_TIME = 30*24*60*60*1000; // 30일 테스트용 (1/1000초)
}
