package com.ssg.starroad.review.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.review.enums.ConfidenceType;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Column
    private String imagePath;
}
