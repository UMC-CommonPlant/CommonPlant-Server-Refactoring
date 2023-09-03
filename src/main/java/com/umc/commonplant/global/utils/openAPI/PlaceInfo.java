package com.umc.commonplant.global.utils.openAPI;


import com.google.gson.annotations.SerializedName;
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
}