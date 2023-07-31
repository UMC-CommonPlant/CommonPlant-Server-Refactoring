package com.umc.commonplant.domain.user.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "user")
@NoArgsConstructor
@Entity
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    private String name;
    private String email;
    private String status;
    private String refreshToken;
    private String provider;
    private String providerId;
    private String imgUrl;
    private String introduction;
    private String uuid;
}
