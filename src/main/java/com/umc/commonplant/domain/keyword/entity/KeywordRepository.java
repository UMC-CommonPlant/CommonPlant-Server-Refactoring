package com.umc.commonplant.domain.keyword.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    @Query("select k.keyword from Keyword k where k.story.storyIdx=?1")
    List<String> findAllByStoryIdx(Long storyIdx);
}
