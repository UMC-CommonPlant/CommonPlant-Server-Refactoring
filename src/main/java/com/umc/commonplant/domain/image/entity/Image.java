package com.umc.commonplant.domain.image.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.Builder;
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
    @Column(name = "img_idx")
    private Long imgIdx;

    private String imgUrl;

    private String category;

    private Long category_idx;

    @Builder
    public Image(String imgUrl, String category, Long category_idx){
        this.imgUrl = imgUrl;
        this.category = category;
        this.category_idx = category_idx;
    }
}
