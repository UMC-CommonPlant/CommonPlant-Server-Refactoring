package com.umc.commonplant.domain.image.controller;


import com.umc.commonplant.domain.image.dto.ImageDto;
import com.umc.commonplant.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Hidden
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/create/images")
    @ResponseStatus(HttpStatus.OK)
    public List<String> createImages(@ModelAttribute ImageDto.ImagesRequest images) {
        return imageService.createImages(images, new ImageDto.ImageRequest("test2", 2L));
    }

    @PostMapping("/create/image")
    @ResponseStatus(HttpStatus.OK)
    public String createImage(@ModelAttribute MultipartFile image) {
        return imageService.createImage(image, new ImageDto.ImageRequest("codetest", 2L));
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public String saveImage(@ModelAttribute MultipartFile image) {
        return imageService.saveImage(image);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getImage(){
        return imageService.findImageUrlByCategory(new ImageDto.ImageRequest("codetest", 2L));
    }

    @DeleteMapping("/delete/image")
    @ResponseStatus(HttpStatus.OK)
    public void deleteImage(){
        imageService.deleteFileInDatabase(new ImageDto.ImageRequest("test2", 2L));
    }

    @DeleteMapping("/delete/one")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOne(){
        imageService.deleteFileInS3("dddd.com/dsafa");
    }
}