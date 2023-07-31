package com.umc.commonplant.domain.alarm.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "alarm")
@NoArgsConstructor
@Entity
public class Alarm extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_idx")
    private Long alarmIdx;
}
