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
        private Long plantIdx;
        @Schema(description = "식물도감에 저장된 식물의 종 이름(학술명)", example = "몬스테라 카스테니안")
        private String name;
        @Schema(description = "식물의 닉네임", example = "몬테")
        private String nickname;
        @Schema(description = "식물이 있는 장소의 이름", example = "카페")
        private String place;
        @Schema(description = "식물의 이미지 url 문자열")
        private String imgUrl;
        @Schema(description = "식물과 함께한 날짜", example = "1")
        private Long countDate;
        @Schema(description = "다음 물주기 D-Day 까지 남은 날짜", example = "10")
        private Long remainderDate;

        // Memo
        @Schema(description = "식물에 작성된 메모 리스트")
        private List<MemoDto.GetAllMemo> memoList;

        // Info
        @Schema(description = "학술명", example = "Monstera carstenianum")
        private String scientificName;
        @Schema(description = "식물의 물주는 주기 (Information 에 저장된 권장 주기)", example = "10")
        private Long waterDay;
        @Schema(description = "관리 팁", example = "물을 좋아하나 과습에 주의하세요!")
        private String tip;
        @Schema(description = "빛의 강도", example = "밝은 곳을 좋아해요!")
        private String sunlight;
        @Schema(description = "최저 온도", example = "16")
        private Long tempMin;
        @Schema(description = "최고 온도", example = "20")
        private Long tempMax;
        @Schema(description = "습도", example = "70% 이상")
        private String humidity;

        @Schema(
                description = "식물이 등록된 날짜",
                example = "2024.01.29"
        )
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime createdAt;

        @Schema(
                description = "사용자가 마지막으로 물을 준 날짜",
                example = "2024.01.29"
        )
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
        private Long plantIdx;
        private String nickname;
        private String imgUrl;
        private String place;
        private String member;
        private Long remainderDate;

        @Builder
        public getPlantListRes(Plant plant, String place, String member, Long remainderDate){
            this.plantIdx = plant.getPlantIdx();
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
        private Long plantIdx;
        private String plantName;
        private String nickname;
        private String imgUrl;
        private String recentMemo;
        private Long remainderDate;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime wateredDate;

        @Builder
        public getMyGardenPlantListRes(Plant plant, Long remainderDate, String recentMemo){
            this.plantIdx = plant.getPlantIdx();
            this.plantName = plant.getPlantName();
            this.nickname = plant.getNickname();
            this.imgUrl = plant.getImgUrl();
            this.wateredDate = plant.getWateredDate();
            this.remainderDate = remainderDate;
            this.recentMemo = recentMemo;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class updatePlantReq{
        private Long plantIdx;
        private String nickname;
    }

    /**
     * 식물 수정할 때 불러올 화면
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class updatePlantRes{
        private Long plantIdx;
        private String nickname;
        private String imgUrl;
    }

    /**
     * 식물 D-Day 업데이트
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class updateWateredDateRes{
        private Long plantIdx;
        private Long remainderDate;
    }

}
