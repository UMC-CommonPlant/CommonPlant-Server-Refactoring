package com.umc.commonplant.domain.history.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain2.info.entity.Info;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "history")
@NoArgsConstructor
@Entity
public class History extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_idx")
    private Long historyIdx;
//    @OneToOne
//    @JoinColumn(name = "info_idx")
    private String name;
    @Column(name = "scientific_name")
    private String scientificName;
    private int count;


}
