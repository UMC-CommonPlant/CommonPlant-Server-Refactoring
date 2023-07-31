package com.umc.commonplant.domain.info.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "info")
@NoArgsConstructor
@Entity
public class Info extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_idx")
    private Long infoIdx;

    private String name;
    private String humidity;
    private String management;
    private String place;
    private String scientific_name;
    private Long water_day;
    private String sunlight;
    private Long temp_max;
    private Long temp_min;
    private String tip;
    private String water_spring;
    private String water_autumn;
    private String water_winter;
    private String water_summer;
    private String imgUrl;
}
