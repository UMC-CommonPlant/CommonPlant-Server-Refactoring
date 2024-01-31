package com.umc.commonplant.global.Jwt;

import lombok.Getter;

@Getter
public class JwtSecret {
    public static String SECRET = "commontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplant";
    public static int EXPIRATION_TIME = 7*24*60*60*1000; // 7일 (1/1000초)

    public static final String BEARER = "Bearer ";
}
