package com.umc.commonplant.domain.keyword.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.story.entity.Story;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "keyword")
@NoArgsConstructor
@Entity
public class Keyword extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_idx")
    private Long keywordIdx;

    @ManyToOne
    @JoinColumn(name = "story_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Story story;

    private String keyword;

    @Builder
    public Keyword(Story story, String keyword) {
        this.story = story;
        this.keyword = keyword;
    }

}
