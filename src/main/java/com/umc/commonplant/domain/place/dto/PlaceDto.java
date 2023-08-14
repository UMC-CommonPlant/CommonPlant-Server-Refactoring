package com.umc.commonplant.domain.place.dto;

import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PlaceDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class createPlaceReq{
        private String name;
        private String address;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class getPlaceRes {
        private String name;
        private String code;
        private String address;
        private String humidity;
        private String maxTemp;
        private String minTemp;
        private boolean isOwner;
        private List<getPlaceResUser> userList;
//        private List<Plant> plantList;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class getPlaceResUser{
        private String name;
        private String image;
    }
}
