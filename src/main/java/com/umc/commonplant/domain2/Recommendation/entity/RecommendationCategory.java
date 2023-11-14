package com.umc.commonplant.domain2.Recommendation.entity;

public enum RecommendationCategory {
    STUDIO("원룸"),
    AIR_PURIFYING("공기정화"),
    BEGINNER_FRIENDLY("초보집사"),
    SHADE_LOVING("어두운 곳을 좋아함"),
    LIGHT_LOVING("밝은 곳을 좋아함"),
    WATER_LOVING("물을 좋아함"),
    DROUGHT_RESISTANT("물을 좋아하지 않음"),
    INTERIOR("인테리어");

    private final String description;

    RecommendationCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
