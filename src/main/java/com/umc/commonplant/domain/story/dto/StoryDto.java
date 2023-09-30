package com.umc.commonplant.domain.story.dto;

import com.umc.commonplant.domain.comment.dto.CommentDto;
import com.umc.commonplant.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class StoryDto {

    @Data
    @NoArgsConstructor
    public static class createStoryReq{
        private String content;
        private List<String> keyword;
    }

    @Data
    @NoArgsConstructor
    public static class getStoryRes {
        private Long storyIdx;
        private String content;
        private getStoryUserRes owner;
        private List<String> keywords;
        private List<String> images;
        private int like;
        private Boolean isLike;
        private List<CommentDto.storyComment> comments;
        private Boolean isOwner;

        @Builder
        public getStoryRes(Long storyIdx, String content, User owner, List<String> keywords, List<String> images,
                           int like, boolean isLike, List<CommentDto.storyComment> comments) {
            this.storyIdx = storyIdx;
            this.content = content;
            this.owner = new getStoryUserRes(owner.getImgUrl(), owner.getName());
            this.keywords = keywords;
            this.images = images;
            this.comments = comments;
            this.like = like;
            this.isLike = isLike;
            this.isOwner = false;
        }

        public void setIsOwner(boolean owner) {
            isOwner = owner;
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getStoryUserRes{
        private String image;
        private String name;
    }
}
