package com.umc.commonplant.domain2.info.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.Builder;
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
    @Column(name = "scientific_name")
    private String scientificName;
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
    private Boolean verified;

    @Builder
    public Info(String name, String humidity, String management, String place, String scientific_name, Long water_day, String sunlight, Long temp_max, Long temp_min, String tip, String water_spring, String water_autumn, String water_winter, String water_summer, String imgUrl, Boolean verified) {
        this.name = name;
        this.humidity = humidity;
        this.management = management;
        this.place = place;
        this.scientificName = scientific_name;
        this.water_day = water_day;
        this.sunlight = sunlight;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.tip = tip;
        this.water_spring = water_spring;
        this.water_autumn = water_autumn;
        this.water_winter = water_winter;
        this.water_summer = water_summer;
        this.imgUrl = imgUrl;
        this.verified = verified;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setId(Long id) { this.infoIdx = id; }
    public void setVerified(Boolean verified) { this.verified = verified; }
}
