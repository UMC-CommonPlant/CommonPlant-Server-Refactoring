package com.umc.commonplant.domain2.info.dto;

import com.umc.commonplant.domain2.info.entity.Info;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class InfoDto {

    @NoArgsConstructor
    @Data
    public static class InfoRequest {
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
        private String water_spring;
        private String water_autumn;
        private String water_winter;
        private String water_summer;

        @Builder
        public InfoRequest(String name, String humidity, String management, String place, String scientific_name, Long water_day, String sunlight, Long temp_max, Long temp_min, String tip, String water_spring, String water_autumn, String water_winter, String water_summer) {
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

        @Builder
        public InfoResponse(String name, String humidity, String management, String place, String scientific_name, Long water_day, String sunlight, Long temp_max, Long temp_min, String tip, String water_type) {
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
        }
    }
}
