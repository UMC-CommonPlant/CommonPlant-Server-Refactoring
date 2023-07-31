package com.umc.commonplant.domain.qna.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "qna")
@NoArgsConstructor
@Entity
public class QnA extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_idx")
    private Long qnaIdx;
    @ManyToOne
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    private String content;
}
