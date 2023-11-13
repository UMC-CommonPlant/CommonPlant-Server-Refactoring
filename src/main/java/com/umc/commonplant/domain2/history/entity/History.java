package com.umc.commonplant.domain2.history.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain2.info.entity.Info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToOne
    @JoinColumn(name = "info_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Info info;
    private int count;

    public void setCount(int count) { this.count = count; }
    public void setInfo(Info info) {this.info = info; }
}
