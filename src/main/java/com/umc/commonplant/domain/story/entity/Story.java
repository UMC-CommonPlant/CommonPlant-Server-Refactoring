package com.umc.commonplant.domain.story.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "story")
@NoArgsConstructor
@Entity
public class Story extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_idx")
    private Long storyIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String is_public;

    @Builder
    public Story(User user, String content, String is_public) {
        this.user = user;
        this.content = content;
        this.is_public = is_public;
    }
}
