package com.umc.commonplant.domain2.Recommendation.entity;

import com.umc.commonplant.domain2.info.entity.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recommendation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"info_id", "category"})
})
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "info_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Info info;

    @Enumerated(EnumType.STRING)
    private RecommendationCategory category;
}
