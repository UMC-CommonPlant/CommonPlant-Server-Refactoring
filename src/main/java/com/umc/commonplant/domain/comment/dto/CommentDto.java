package com.umc.commonplant.domain.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public class CommentDto {
    @Data
    @NoArgsConstructor
    public static class createReq{
        private Long storyIdx;
        private Long parentIdx;
        private String content;
    }

}
