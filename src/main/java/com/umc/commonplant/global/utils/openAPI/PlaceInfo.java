package com.umc.commonplant.global.utils.openAPI;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class PlaceInfo {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class LatXLngY
    {
        public double lat;
        public double lng;

        public double x;
        public double y;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class weatherInfo
    {
        private String highestTemp;    // 최고기온
        private String minimumTemp;   // 최저기온
        private String humidity;     // 습도
    }
}