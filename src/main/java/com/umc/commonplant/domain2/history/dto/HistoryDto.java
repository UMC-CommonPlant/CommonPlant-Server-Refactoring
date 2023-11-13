package com.umc.commonplant.domain2.history.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class HistoryDto {
    @NoArgsConstructor
    @Data
    public static class GetHistoryResponse {
        private LocalDateTime criterionTime;
        private String scientific_name;
        private String imgUrl;

        @Builder
        public GetHistoryResponse(String name, String scientific_name, String imgUrl) {
            //this.name = name;
            this.scientific_name = scientific_name;
            this.imgUrl = imgUrl;
        }

    }
}

