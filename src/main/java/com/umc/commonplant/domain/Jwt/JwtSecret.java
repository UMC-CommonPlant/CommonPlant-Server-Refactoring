package com.umc.commonplant.domain.Jwt;

import lombok.Getter;

@Getter
public class JwtSecret {
    public static String SECRET = "commontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplantcommontplant";
    public static int EXPIRATION_TIME = 7*24*60*60*1000; // 7일 (1/1000초)

    public static String accessHeader = "Authorization";
    public static String refreshHeader = "Authorization-refresh";

    public static final String BEARER = "Bearer ";
}
