package com.umc.commonplant.domain.memo.controller;

import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.memo.dto.MemoDto;
import com.umc.commonplant.domain.memo.service.MemoService;
import com.umc.commonplant.domain.story.dto.StoryDto;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.dto.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequestMapping("/memo")
@RequiredArgsConstructor
@RestController
public class MemoController {

    private final MemoService memoService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<JsonResponse> createMemo(@RequestPart("memoRequest")MemoDto.MemoRequest memoRequest, @RequestPart("image") MultipartFile multipartFile){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        memoService.createMemo(user, memoRequest, multipartFile);

        return ResponseEntity.ok(new JsonResponse(true, 200, "createMemo", null));
    }

    @PatchMapping("/update")
    public ResponseEntity<JsonResponse> updateMemo(@RequestPart("memoRequest")MemoDto.MemoUpdateRequest memoUpdateRequest, @RequestPart("image") MultipartFile multipartFile) {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        memoService.updateMemo(user, memoUpdateRequest, multipartFile);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updateMemo", null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<JsonResponse> deleteMemo(@RequestParam("memo_idx") Long memo_idx) {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        memoService.deleteMemo(user, memo_idx);

        return ResponseEntity.ok(new JsonResponse(true, 200, "deleteMemo", null));
    }

    @GetMapping("/one-memo")
    public ResponseEntity<JsonResponse> getOneMemo(@RequestParam("memo_idx") Long memo_idx) {
        MemoDto.GetOneMemo memoResponse = memoService.getOneMemo(memo_idx);
        return ResponseEntity.ok(new JsonResponse(true, 200, "get one-memo", memoResponse));
    }

    @GetMapping("/get-plantmemo")
    public ResponseEntity<JsonResponse> getPlantMemo(@RequestParam("plant_idx") Long plant_idx) {
        List<MemoDto.GetAllMemo> memoResponseList = memoService.getAllMemoByPlant(plant_idx);
        return ResponseEntity.ok(new JsonResponse(true, 200, "get one-memo", memoResponseList));
    }

}
