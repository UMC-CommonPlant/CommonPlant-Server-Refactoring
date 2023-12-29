package com.umc.commonplant.domain.place.dto;

import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.plant.dto.PlantDto;
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
        private boolean isOwner;
        private List<getPlaceResUser> userList;
        private List<PlantDto.getMyGardenPlantListRes> plantList;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class getPlaceResUser{
        private String name;
        private String image;
    }


    @Data
    @Builder
    public static class getWeatherRes{
        private String maxTemp;
        private String minTemp;
        private String humidity;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getPlaceGridRes {
        private String nx;
        private String ny;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class newFriendsReq{
        private String name;
        private String code;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class getFriendsReq{
        private String name;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class getPlaceListRes{
        private String image;
        private String code;
        private String name;
        private String member;
        private String plant;
        public getPlaceListRes(Place place, String member, String plant){
            this.image = place.getImgUrl();
            this.code = place.getCode();
            this.name = place.getName();
            this.member = member;
            this.plant = plant;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class getMainPage{
        private String name;
        private List<getPlaceListRes> placeList;
        private List<PlantDto.getPlantListRes> plantList;
    }
}
