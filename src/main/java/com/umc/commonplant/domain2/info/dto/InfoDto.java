package com.umc.commonplant.domain2.info.dto;

import com.umc.commonplant.domain2.info.entity.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class InfoDto {

    @NoArgsConstructor
    @Data
    public static class InfoRequest {
        @NotNull
        @Schema(description = "식물 이름", example = "식물")
        private String name;

        @Schema(description = "습도", hidden = true)
        private String humidity;

        @Schema(description = "관리 방법", hidden = true)
        private String management;

        @Schema(description = "키우기 좋은 위치", hidden = true)
        private String place;

        @Schema(description = "학술명", example = "plant", hidden = true)
        private String scientific_name;
        @Schema(description = "물주는 주기", example = "10", hidden = true)
        private Long water_day;
        @Schema(description = "빛의 강도", hidden = true)
        private String sunlight;
        @Schema(description = "최대 온도", hidden = true)
        private Long temp_max;
        @Schema(description = "최저 온도", hidden = true)
        private Long temp_min;
        @Schema(description = "관리 팁", hidden = true)
        private String tip;
        @Schema(description = "봄 관리법", hidden = true)
        private String water_spring;
        @Schema(description = "가을 관리법", hidden = true)
        private String water_autumn;
        @Schema(description = "겨울 관리법", hidden = true)
        private String water_winter;
        @Schema(description = "여름 관리법", hidden = true)
        private String water_summer;
        @Schema(description = "검증 여부", hidden = true)
        private Boolean verified;

        @Builder
        public InfoRequest(String name, String humidity, String management, String place, String scientific_name, Long water_day, String sunlight, Long temp_max, Long temp_min, String tip, String water_spring, String water_autumn, String water_winter, String water_summer, Boolean verified) {
            this.name = name;
            this.humidity = humidity;
            this.management = management;
            this.place = place;
            this.scientific_name = scientific_name;
            this.water_day = water_day;
            this.sunlight = sunlight;
            this.temp_max = temp_max;
            this.temp_min = temp_min;
            this.tip = tip;
            this.water_spring = water_spring;
            this.water_autumn = water_autumn;
            this.water_winter = water_winter;
            this.water_summer = water_summer;
            this.verified = verified;
        }

        public Info toEntity(){
            return Info.builder()
                    .name(name)
                    .humidity(humidity)
                    .management(management)
                    .place(place)
                    .scientific_name(scientific_name)
                    .water_day(water_day)
                    .sunlight(sunlight)
                    .temp_max(temp_max)
                    .temp_min(temp_min)
                    .tip(tip)
                    .water_spring(water_spring)
                    .water_autumn(water_autumn)
                    .water_winter(water_winter)
                    .water_summer(water_summer)
                    .verified(verified)
                    .build();
        }
    }

    @NoArgsConstructor
    @Data
    public static class InfoResponse {
        private String name;
        private String humidity;
        private String management;
        private String place;
        private String scientific_name;
        private Long water_day;
        private String sunlight;
        private Long temp_max;
        private Long temp_min;
        private String tip;
        private String water_type;
        private String imgUrl;

        @Builder
        public InfoResponse(String name, String humidity, String management, String place, String scientific_name, Long water_day, String sunlight, Long temp_max, Long temp_min, String tip, String water_type, String imgUrl) {
            this.name = name;
            this.humidity = humidity;
            this.management = management;
            this.place = place;
            this.scientific_name = scientific_name;
            this.water_day = water_day;
            this.sunlight = sunlight;
            this.temp_max = temp_max;
            this.temp_min = temp_min;
            this.tip = tip;
            this.water_type = water_type;
            this.imgUrl = imgUrl;
        }
    }

    @NoArgsConstructor
    @Data
    public static class SearchInfoResponse {
        private String name;
        private String scientific_name;
        private String imgUrl;

        @Builder
        public SearchInfoResponse(String name, String scientific_name, String imgUrl) {
            this.name = name;
            this.scientific_name = scientific_name;
            this.imgUrl = imgUrl;
        }

    }
}
