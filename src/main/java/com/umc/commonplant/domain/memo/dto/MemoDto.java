package com.umc.commonplant.domain.memo.dto;

import com.umc.commonplant.domain.memo.entity.Memo;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain2.info.entity.Info;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemoDto {

    @NoArgsConstructor
    @Data
    public static class MemoRequest {
        private Long plant_idx;
        private String content;

        @Builder
        public MemoRequest(Long plant_idx, String content) {
            this.plant_idx = plant_idx;
            this.content = content;
        }

    }

}
