package com.umc.commonplant.global.utils.openAPI;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WeatherResult {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonDeserialize(using = WeatherDeserializer.class)
    public static class items{
        private List<item> item;
    }

    @Data
    @NoArgsConstructor
    public static class item{
        private String baseDate;
        private String baseTime;
        private String category;
        private String fcstDate;
        private String fcstTime;
        private String fcstValue;
        private Long nx;
        private Long ny;
    }

}
