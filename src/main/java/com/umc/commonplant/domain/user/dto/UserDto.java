package com.umc.commonplant.domain.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
public class UserDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class join{
        @Schema(description = "회원가입할 사용자 email", example= "test@test.com")
        private String email;
        @Schema(description = "회원가입할 사용자 이름", example= "커먼")
        private  String name;
        @Schema(description = "소셜로그인", example = "kakao")
        private String provider;
        @Schema(description = "회원번호", example = "0123456789")
        private String providerId;
    }
    @NotBlank(message = "사용할 이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "이름은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private  String name;
}
