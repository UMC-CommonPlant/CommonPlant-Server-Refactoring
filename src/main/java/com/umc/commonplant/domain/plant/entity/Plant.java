package com.umc.commonplant.domain.plant.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "plant")
@NoArgsConstructor
@Entity
public class Plant extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_idx")
    private Long plantIdx;

    @ManyToOne
    @JoinColumn(name = "place_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @Column(nullable = false)
    private String plantName;
    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false)
    private String imgUrl;
    private int waterCycle;
    private LocalDateTime wateredDate;

    @Builder
    public Plant(Place place, String plantName, String nickname, int waterCycle, String imgUrl, LocalDateTime wateredDate){
        this.place = place;
        this.plantName = plantName;
        this.nickname = nickname;
        this.waterCycle = waterCycle;
        this.imgUrl = imgUrl;
        this.wateredDate = wateredDate;
    }

    /**
     * 식물 수정 API 관련 메소드
     */
    public void updatePlant(String imgUrl, String nickname) {
        this.imgUrl = imgUrl;
        this.nickname = nickname;
    }

    /**
     * 식물 D-Day 수정 API 관련 메소드
     */
    public void setWateredDate(LocalDateTime wateredDate){
        this.wateredDate = wateredDate;
    }

}
