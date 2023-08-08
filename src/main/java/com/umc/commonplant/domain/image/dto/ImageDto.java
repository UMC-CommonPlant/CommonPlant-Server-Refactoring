package com.umc.commonplant.domain.image.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ImageDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ImageRequest{
        private String category;
        private Long category_idx;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    public static class ImagesRequest{
        private List<MultipartFile> images = new ArrayList<>();
    }
}
