package com.umc.commonplant.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PlaceDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class create{
        private String name;
        private String address;
    }
}
