package com.umc.commonplant.domain.comment.dto;

import com.umc.commonplant.domain.comment.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDto {
    @Data
    @NoArgsConstructor
    public static class createReq{
        private Long storyIdx;
        private Long parentIdx;
        private String content;
    }

    @Data
    @NoArgsConstructor
    public static class storyComment {
        private Long commnetIdx;
        private String userName;
        private String userImage;
        private String content;
        private LocalDateTime createdDate;
        private List<ChildCommentByStory> child = new ArrayList<>();

        public storyComment(Comment c) {
            this.commnetIdx = c.getCommentIdx();
            this.userName = c.getUser().getName();
            this.userImage = c.getUser().getImgUrl();
            this.content = c.getContent();
            this.createdDate = c.getCreatedAt();
            if(c.getChild() !=  null){
                c.getChild().forEach(comment -> {
                    child.add(new ChildCommentByStory(comment));
                });
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class ChildCommentByStory{
        private Long commnetIdx;
        private String userName;
        private String userImage;
        private String content;
        private LocalDateTime createdDate;

        public ChildCommentByStory(Comment c) {
            this.commnetIdx = c.getCommentIdx();
            this.userName = c.getUser().getName();
            this.userImage = c.getUser().getImgUrl();
            this.content = c.getContent();
            this.createdDate = c.getCreatedAt();
        }
    }
}
