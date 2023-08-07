package com.umc.commonplant.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ImageDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ImageRequest{
        private String category;
        private Long category_idx;
    }

    public static class ImagesRequest{
        
    }
}
