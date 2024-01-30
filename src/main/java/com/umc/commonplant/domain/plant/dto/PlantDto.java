package com.umc.commonplant.domain.plant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.commonplant.domain.memo.dto.MemoDto;
import com.umc.commonplant.domain.plant.entity.Plant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PlantDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class createPlantReq{
        @Schema(description = "식물도감에 저장된 식물의 종 이름(학술명)", example = "몬스테라 카스테니안")
        private String plantName;
        @Schema(description = "식물의 닉네임", example = "몬테")
        private String nickname;
        // TODO: 장소를 추가할 때 장소 리스트에서 선택하는 것이기 때문에 추후 수정/삭제
        @Schema(description = "식물을 추가할 장소의 코드 문자열 값", example = "uCaoaq")
        private String place;
        // TODO: (default)식물 도감에 있는 값(info.getWaterDay()), 사용자가 물주기 기간 설정 가능
        // private int waterCycle;
        @Schema(
                description = "사용자가 최초로 설정하는 식물의 물주기 기간 값(식물에 물을 어느 주기로 줄 것인지 설정). " +
                "만약 따로 설정하지 않는다면 식물 추가 API를 실행할 때, 식물도감에 저장된 권장 물주기 값으로 설정됨. " +
                "입력은 문자열로 받음",
                example = "15"
        )
        private String waterCycle;
        @Schema(
                description = "사용자가 마지막으로 물을 준 날짜. 입력은 문자열로 받으며, 형식은 example과 같음",
                example = "2024.01.29"
        )
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
        private String place;
        private String member;
        private Long remainderDate;

        @Builder
        public getPlantListRes(Plant plant, String place, String member, Long remainderDate){
            this.nickname = plant.getNickname();
            this.imgUrl = plant.getImgUrl();
            this.place = place;
            this.member = member;
            this.remainderDate = remainderDate;
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
