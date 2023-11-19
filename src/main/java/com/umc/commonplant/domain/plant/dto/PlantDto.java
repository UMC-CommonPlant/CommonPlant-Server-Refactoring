package com.umc.commonplant.domain.plant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.commonplant.domain.memo.dto.MemoDto;
import com.umc.commonplant.domain.plant.entity.Plant;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PlantDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class createPlantReq{
        private String plantName;
        private String nickname;
        // TODO: 장소를 추가할 때 장소 리스트에서 선택하는 것이기 때문에 추후 수정/삭제
        private String place;
        // TODO: (default)식물 도감에 있는 값(info.getWaterDay()), 사용자가 물주기 기간 설정 가능
        // private int waterCycle;
        private String waterCycle;
        private String strWateredDate;
        // TODO: 물주는 날짜가 적게 남은 순서로 정렬
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getPlantRes{
        private String name;
        private String nickname;

        private String place;

        private String imgUrl;

        private Long countDate;
        private Long remainderDate;

        // Memo
        private List<MemoDto.GetAllMemo> memoList;

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

    /**
     * Main Page, My Calendar에 보여줄 Plant 리스트
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getPlantListRes{
        private String nickname;
        private String imgUrl;

        @Builder
        public getPlantListRes(Plant plant){
            this.nickname = plant.getNickname();
            this.imgUrl = plant.getImgUrl();
        }
    }

    /**
     * My Garden에 보여줄 Plant 리스트
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getMyGardenPlantListRes{
        private String plantName;
        private String nickname;
        private String imgUrl;
        private String recentMemo;
        private Long remainderDate;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime wateredDate;

        @Builder
        public getMyGardenPlantListRes(Plant plant, Long remainderDate, String recentMemo){
            this.plantName = plant.getPlantName();
            this.nickname = plant.getNickname();
            this.imgUrl = plant.getImgUrl();
            this.wateredDate = plant.getWateredDate();
            this.remainderDate = remainderDate;
            this.recentMemo = recentMemo;
        }
    }

//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Data
//    public static class updatePlantReq{
//        private String nickname;
//    }

    /**
     * 식물 수정할 때 불러올 화면
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class updatePlantRes{
        private String nickname;
        private String imgUrl;
    }
}
