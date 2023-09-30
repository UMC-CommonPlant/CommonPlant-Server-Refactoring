package com.umc.commonplant.domain.likes.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.likes.service.LikeService;
import com.umc.commonplant.domain.story.service.StoryService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;
    private final StoryService storyService;
    private final JwtService jwtService;
    private final UserService userService;

    @RequestMapping("/story/{storyIdx}")
    public ResponseEntity<JsonResponse> likeStory(@PathVariable Long storyIdx){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        storyService.findStoryByIdx(storyIdx);
        likeService.likeStory(user, storyIdx);

        return ResponseEntity.ok(new JsonResponse(true, 200, "likeStory", null));
    }
}
