package com.umc.commonplant.domain.heart.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "heart")
@NoArgsConstructor
@Entity
public class Heart extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_idx")
    private Long heartIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private String category;
    private Long categoryIdx;

}
