package com.umc.commonplant.domain.story.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.story.dto.StoryDto;
import com.umc.commonplant.domain.story.service.StoryService;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/story")
@RestController
public class StoryController {
    private final JwtService jwtService;
    private final UserService userService;
    private final StoryService storyService;

    // TODO : createStory : 스토리 게시글 등록
    @PostMapping("/create")
    public ResponseEntity<JsonResponse> createStory(@RequestPart("story") StoryDto.createStoryReq req,
                                                    @RequestPart("images") List<MultipartFile> imageList){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        storyService.createStory(user, req, imageList);
        return ResponseEntity.ok(new JsonResponse(true, 200, "createStory", null));
    }

    // TODO : getStory : 스토리 게시글 상세 보기
    @GetMapping("/{storyIdx}")
    public ResponseEntity<JsonResponse> getStory(@PathVariable Long storyIdx){
        //게스트 유저가 접근시 처리 방법 필요
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        StoryDto.getStoryRes res = storyService.getStory(user, storyIdx);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getStory", res));
    }


    // TODO : getStoryList : 스토리 메인 피드
    // TODO : likeStory : 스토리 좋아요

}
