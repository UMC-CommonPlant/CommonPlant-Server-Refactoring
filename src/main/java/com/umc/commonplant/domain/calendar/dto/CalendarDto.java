package com.umc.commonplant.domain.calendar.dto;

import com.umc.commonplant.domain.plant.entity.Plant;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CalendarDto {

    /**
     * 최종 My Calendar Response
     */
    @NoArgsConstructor
    @Getter
    public static class getMyCalendarRes{
        private String year;
        private String month;

        private List<getMyCalendarEventRes> dateList;

        @Builder
        public getMyCalendarRes(String year, String month, List<getMyCalendarEventRes> dateList) {
            this.year = year;
            this.month = month;
            this.dateList = dateList;
        }
    }

    /**
     * 날짜 별로 불러오는 My Calendar 정보
     */
    @NoArgsConstructor
    @Getter
    public static class getMyCalendarEventRes {
        private int parsedDate;
//        @JsonFormat(pattern = "yyyy.MM.dd")
//        private LocalDateTime date;

        private boolean nextWatered;
        private boolean prevWatered;
        private boolean joinPlant;
        private boolean writeMemo;

        @Builder
        public getMyCalendarEventRes(int parsedDate, boolean nextWatered, boolean prevWatered,
                                     boolean joinPlant, boolean writeMemo) {
            this.parsedDate = parsedDate;
            this.nextWatered = nextWatered;
            this.prevWatered = prevWatered;
            this.joinPlant = joinPlant;
            this.writeMemo = writeMemo;
        }
    }
    
    /**
     * My Calendar에 보여줄 Place 리스트
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getMyCalendarPlaceListRes{
        private String name;
    }

    /**
     * My Calendar에 보여줄 Plant 리스트
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getMyCalendarPlantListRes{
        private String plantName;
        private String nickname;
        private String imgUrl;

        @Builder
        public getMyCalendarPlantListRes(Plant plant){
            this.plantName = plant.getPlantName();
            this.nickname = plant.getNickname();
            this.imgUrl = plant.getImgUrl();
        }
    }

    /**
     * My Calendar에 보여줄 Memo 리스트
     */
    @NoArgsConstructor
    @Getter
    public static class getMyCalendarMemoRes {
        private Long memo_idx;
        private String content;
        private String imgUrl;
        private String writer;
        private LocalDateTime created_at;

        @Builder
        public getMyCalendarMemoRes(Long memo_idx, String content, String imgUrl, String writer, LocalDateTime created_at) {
            this.memo_idx = memo_idx;
            this.content = content;
            this.imgUrl = imgUrl;
            this.writer = writer;
            this.created_at = created_at;
        }
    }

}
