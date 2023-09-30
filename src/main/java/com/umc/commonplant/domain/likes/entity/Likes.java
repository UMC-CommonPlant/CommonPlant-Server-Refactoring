package com.umc.commonplant.domain.likes.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "likes")
@NoArgsConstructor
@Entity
public class Likes extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_idx")
    private Long likeIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private String category;
    private Long categoryIdx;

    @Builder
    public Likes(User user, String category, Long categoryIdx) {
        this.user = user;
        this.category = category;
        this.categoryIdx = categoryIdx;
    }
}
