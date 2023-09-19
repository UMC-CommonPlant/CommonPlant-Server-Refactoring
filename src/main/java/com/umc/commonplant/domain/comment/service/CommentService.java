package com.umc.commonplant.domain.comment.service;

import com.umc.commonplant.domain.comment.dto.CommentDto;
import com.umc.commonplant.domain.comment.entity.Comment;
import com.umc.commonplant.domain.comment.entity.CommentRepository;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.NOT_FOUNT_PARENT_COMMENT;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public void createStoryComment(User user, CommentDto.createReq req) {
        Comment comment = Comment.builder()
                .user(user).category("STORY").categoryIdx(req.getStoryIdx())
                .content(req.getContent())
                .build();

        Comment parentComment;
        if(req.getParentIdx() != null){
            parentComment = commentRepository.findParentCommentByIdx(req.getParentIdx())
                    .orElseThrow(() ->  new BadRequestException(NOT_FOUNT_PARENT_COMMENT));
            comment.updateParent(parentComment);
        }
        commentRepository.save(comment);
    }

    public List<CommentDto.storyComment> getCommentByStory(Long storyIdx){
        List<Comment> comments = commentRepository.getCommentByStory(storyIdx);
        List<CommentDto.storyComment> commentDtoList = new ArrayList<>();
        comments.forEach( c -> {
            commentDtoList.add(new CommentDto.storyComment(c));
        });
        return commentDtoList;
    }

}
