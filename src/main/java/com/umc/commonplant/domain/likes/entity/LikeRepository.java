package com.umc.commonplant.domain.likes.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    @Query("select l from Likes l where l.user.userIdx=?1 and l.category='STORY' and l.categoryIdx=?2")
    Likes findHeartByStory(Long userIdx, Long storyIdx);

    @Query("select count(l) from Likes l where l.category='STORY' and l.categoryIdx=?1")
    int countLikeByStory(Long storyIdx);
}
