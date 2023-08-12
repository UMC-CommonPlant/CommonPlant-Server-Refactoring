package com.umc.commonplant.domain.image.controller;


import com.umc.commonplant.domain.image.dto.ImageDto;
import com.umc.commonplant.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController { //이미지 테스트용 controller

    private final ImageService imageService;

    @PostMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    public List<String> saveImage(@ModelAttribute ImageDto.ImagesRequest images) {
        return imageService.createImages(images, new ImageDto.ImageRequest("codetest", 2L));
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getImage(){
        return imageService.findImageUrlByCategory(new ImageDto.ImageRequest("codetest", 2L));
    }

    @DeleteMapping("/delete/image")
    @ResponseStatus(HttpStatus.OK)
    public void deleteImage(){
        imageService.deleteFileInDatabase(new ImageDto.ImageRequest("codetest", 2L));
    }
}