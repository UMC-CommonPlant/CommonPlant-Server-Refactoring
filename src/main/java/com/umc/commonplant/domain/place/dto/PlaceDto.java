package com.umc.commonplant.domain.place.dto;

import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PlaceDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "장소 생성 관련 Request")
    public static class createPlaceReq{
        @Schema(description = "장소 이름" , example = "우리집 거실")
        private String name;
        @Schema(description = "장소 주소" , example = "서울특별시 노원구 광운로 20")
        private String address;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "장소 생성 관련 Request")
    public static class updatePlaceReq{
        @Schema(description = "장소 이름" , example = "우리집 거실")
        private String name;
        @Schema(description = "장소 주소" , example = "서울특별시 노원구 광운로 20")
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
        @Schema(description = "유저 이름" , example = "user1")
        private String name;
        @Schema(description = "장소 코드" , example = "vErDfX")
        private String code;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class getFriendsReq{
        @Schema(description = "유저 이름" , example = "user1")
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class getPlaceBelongUser {
        private String name;
        private String imgUrl;
        private LocalDateTime createdAt;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class getPlaceFriends {
        private boolean isLeader = false;
        private String image;
        private String name;

        public getPlaceFriends(String image, String name) {
            this.image = image;
            this.name = name;
        }

        public void setLeader(boolean leader) {
            isLeader = leader;
        }
    }


}
