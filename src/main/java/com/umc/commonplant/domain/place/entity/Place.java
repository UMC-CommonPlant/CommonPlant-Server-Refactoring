package com.umc.commonplant.domain.place.entity;

import com.umc.commonplant.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "place")
@NoArgsConstructor
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_idx")
    private Long placeIdx;

    @Column(nullable = false)     // 장소 이름
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = true)
    private String girdX;    // 경도 (x)

    @Column(nullable = true)
    private String gridY;     // 위도 (y)

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private String imgUrl;

    @Column(nullable = false)
    private String code;
}
