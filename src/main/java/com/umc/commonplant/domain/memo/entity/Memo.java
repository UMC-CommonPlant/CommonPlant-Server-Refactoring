package com.umc.commonplant.domain.memo.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Builder
@Table(name = "memo")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Memo extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_idx")
    private Long memoIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "plant_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;

    private String imgUrl;

    private String content;
}
