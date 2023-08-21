package com.umc.commonplant.domain.plant.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain2.info.entity.Info;
import com.umc.commonplant.domain.place.entity.Place;
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

    private String name;
    private String imgUrl;
    private LocalDateTime wateredDate;

}
