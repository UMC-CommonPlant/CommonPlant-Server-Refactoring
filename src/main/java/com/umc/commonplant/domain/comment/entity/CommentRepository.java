package com.umc.commonplant.domain.comment.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.depth = 1 and c.commentIdx =?1")
    Optional<Comment> findParentCommentByIdx(Long parentIdx);

    @Query("select c from Comment c where c.depth = 1 and c.category='STORY' and c.categoryIdx=?1")
    List<Comment> getCommentByStory(Long storyIdx);
}
