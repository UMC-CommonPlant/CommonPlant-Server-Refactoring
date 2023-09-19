package com.umc.commonplant.domain.comment.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.comment.dto.CommentDto;
import com.umc.commonplant.domain.comment.entity.Comment;
import com.umc.commonplant.domain.comment.service.CommentService;
import com.umc.commonplant.domain.story.entity.Story;
import com.umc.commonplant.domain.story.service.StoryService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final JwtService jwtService;
    private final UserService userService;
    private final StoryService storyService;

    //TODO : create story comment
    @PostMapping("/create/story")
    public ResponseEntity<JsonResponse> createStoryComment(@RequestBody CommentDto.createReq req){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        storyService.findStoryByIdx(req.getStoryIdx());
        commentService.createStoryComment(user, req);
        return ResponseEntity.ok(new JsonResponse(true, 200, "createStoryComment", null));
    }

    @GetMapping("/story/{storyIdx}")
    public ResponseEntity<JsonResponse> getCommentListTest(@PathVariable Long storyIdx){
        List<CommentDto.storyComment> res = commentService.getCommentByStory(storyIdx);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getCommentListTest", res));
    }
}
