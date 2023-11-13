package com.umc.commonplant.domain2.Recommendation.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByCategory(RecommendationCategory category);

    @Query("SELECT i FROM Recommendation i WHERE i.info.id = :infoId AND i.category = :category")
    Optional<Recommendation> findByInfoIdAndCategory(@Param("infoId") Long infoId, @Param("category") RecommendationCategory category);
}
