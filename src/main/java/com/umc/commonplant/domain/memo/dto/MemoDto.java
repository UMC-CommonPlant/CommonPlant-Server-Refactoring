package com.umc.commonplant.domain.memo.dto;

import com.umc.commonplant.domain.memo.entity.Memo;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain2.info.entity.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemoDto {

    @NoArgsConstructor
    @Data
    public static class MemoRequest {

        @Schema(description = "메모를 작성할 식물 index", example = "1")
        private Long plant_idx;
        @Schema(description = "메모 내용", example = "test")
        private String content;

        @Builder
        public MemoRequest(Long plant_idx, String content) {
            this.plant_idx = plant_idx;
            this.content = content;
        }

    }

    @NoArgsConstructor
    @Data
    public static class MemoUpdateRequest {

        @Schema(description = "수정할 메모의 index", example = "1")
        private Long memo_idx;
        @Schema(description = "수정할 메모가 포함된 식물 index", example = "1")
        private Long plant_idx;
        @Schema(description = "메모 내용", example = "test")
        private String content;
        @Schema(description = "메모 이미지", example = "메모의 이미지를 수정하지 않고 유지하기 위해 전송하는 이전 메모 이미지 url")
        private String imgUrl;

        @Builder
        public MemoUpdateRequest(Long memo_idx, Long plant_idx, String content, String imgUrl) {
            this.memo_idx = memo_idx;
            this.plant_idx = plant_idx;
            this.content = content;
            this.imgUrl = imgUrl;
        }

    }

    @NoArgsConstructor
    @Data
    public static class GetOneMemo {
        private Long memo_idx;
        private Long plant_idx;
        private String content;
        private String imgUrl;

        @Builder
        public GetOneMemo(Long memo_idx, Long plant_idx, String content, String imgUrl) {
            this.memo_idx = memo_idx;
            this.plant_idx = plant_idx;
            this.content = content;
            this.imgUrl = imgUrl;
        }

    }

    @NoArgsConstructor
    @Data
    public static class GetAllMemo {
        private Long memo_idx;
        private String content;
        private String imgUrl;
        private String writer;
        private LocalDateTime created_at;

        @Builder
        public GetAllMemo(Long memo_idx, String content, String imgUrl, String writer, LocalDateTime created_at) {
            this.memo_idx = memo_idx;
            this.content = content;
            this.imgUrl = imgUrl;
            this.writer = writer;
            this.created_at = created_at;
        }

    }

}
