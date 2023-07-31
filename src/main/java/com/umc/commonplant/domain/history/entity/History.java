package com.umc.commonplant.domain.history.entity;

import com.umc.commonplant.domain.BaseTime;
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
    private String word;
    private int count;


}
