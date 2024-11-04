package com.umc.commonplant.domain.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class FriendDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "친구 요청 관련 Request")
    public static class sendFriendReq{
        @Schema(description = "친구 요청 보내는 사람", example = "커먼1")
        private String senderName;
        @Schema(description = "친구 요청 받는 사람", example = "[\"커먼2\", \"커먼3\"]")
        private List<String> receiverName = new ArrayList<>();
        @Schema(description = "친구 요청 상태", example = "PENDING")
        private String status = "PENDING"; // 요청 상태: PENDING, ACCEPTED, REJECTED
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "장소등록 및 친구 요청 관련 Request")
    public static class placeCodeAndFriendResponse{
        @Schema(description = "장소 코드", example = "aBcDeF")
        private String placeCode;
        @Schema(description = "친구요청 받는 사람", example = "[\"커먼2\", \"커먼3\"]")
        private List<String> receiverList;
    }
}
