package com.umc.commonplant.domain.plant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

public class PlantDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class createPlantReq{
        // private String plantImage;
        // TODO: 종 이름을 추가할 때 나중에 Information 에서 불러오기 때문에 추후 수정/삭제
        // private String scientificName;
        private String nickname;
        // TODO: 장소를 추가할 때 장소 리스트에서 선택하는 것이기 때문에 추후 수정/삭제
        private String place;
        // TODO: (default)식물 도감에 있는 값(info.getWaterDay()), 사용자가 물주기 기간 설정 가능
        private int waterCycle;
        private String strWateredDate;
        // TODO: 물주는 날짜가 적게 남은 순서로 정렬
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    // @Builder
    public static class getPlantRes{
        private String name;
        private String nickname;

        private String place;

        private String imgUrl;

        private Long countDate;
        private Long remainderDate;

        // Info
        private String scientificName;
        private Long waterDay;
        private String sunlight;
        private Long tempMin;
        private Long tempMax;
        private String humidity;

        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime wateredDate;
    }

}
