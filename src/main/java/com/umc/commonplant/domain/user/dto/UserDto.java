package com.umc.commonplant.domain.user.dto;


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
        private String email;
        private  String name;
        private String provider;
    }
    @NotBlank(message = "사용할 이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "이름은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private  String name;
}
