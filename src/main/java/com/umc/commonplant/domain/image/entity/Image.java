package com.umc.commonplant.domain.image.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "image")
@NoArgsConstructor
@Entity
public class Image extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_idx")
    private Long infoIdx;

    private String imgUrl;

    private String category;

    private Long category_idx;
}
