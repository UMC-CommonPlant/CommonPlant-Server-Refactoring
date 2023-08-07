package com.umc.commonplant.domain.image.controller;


import com.umc.commonplant.domain.image.dto.ImageDto;
import com.umc.commonplant.domain.image.dto.ImagesDto;
import com.umc.commonplant.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    public List<String> saveImage(@ModelAttribute ImagesDto imageSaveDto) {
        return imageService.saveImages(imageSaveDto, new ImageDto.ImageRequest("codetest", 1L));
    }
}