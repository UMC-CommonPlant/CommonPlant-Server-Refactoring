package com.umc.commonplant.domain.comment.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    //parent(1), child(2)
    @Column(name = "depth")
    private int depth;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_idx")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private String category;

    @Column(name = "category_idx")
    private Long categoryIdx;

    private String content;

    @Builder
    public Comment(User user, String category, Long categoryIdx, String content) {
        this.user = user;
        this.depth = 1;
        this.category = category;
        this.categoryIdx = categoryIdx;
        this.content = content;
    }

    public void updateParent(Comment parentComment) {
        this.depth = 2;
        this.parent = parentComment;
    }
}
