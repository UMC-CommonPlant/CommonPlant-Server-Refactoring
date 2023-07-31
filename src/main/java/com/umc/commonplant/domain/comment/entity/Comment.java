package com.umc.commonplant.domain.comment.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Table(name = "comment")
@NoArgsConstructor
@Entity
public class Comment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_idx")
    private Long commentIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private boolean is_parent;
    @Column(nullable = true)
    private Long parentIdx;
    private String category;
    private String category_idx;
    private String content;


}
