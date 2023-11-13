package com.umc.commonplant.domain2.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryDto {

    @Data
    @AllArgsConstructor
    public static class HistoryResponse {
        private LocalDate firstDayOfMonth;
        private List<EachHistoryDto> historyDtoList;

    }

    @NoArgsConstructor
    @Data
    public static class EachHistoryDto {
        private String name;
        private String scientific_name;
        private String imgUrl;
        private int count;

        @Builder
        public EachHistoryDto(String name, String scientific_name, String imgUrl, int count) {
            this.name = name;
            this.scientific_name = scientific_name;
            this.imgUrl = imgUrl;
            this.count = count;
        }

    }
}

