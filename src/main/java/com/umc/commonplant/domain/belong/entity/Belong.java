package com.umc.commonplant.domain.belong.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Table(name = "belong")
@NoArgsConstructor
@Entity
public class Belong extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "belong_idx")
    private Long belongIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "place_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @Builder
    public Belong(User user, Place place) {
        this.user = user;
        this.place = place;
    }
}
