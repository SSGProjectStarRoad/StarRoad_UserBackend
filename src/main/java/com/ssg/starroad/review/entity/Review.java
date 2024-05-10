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
@Embeddable
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store; // 스토어 이름

    private boolean visible; // 삭제여부

    private Long likeCount; // 좋아요 수

    private String paymentNum;

    private String contents; // 리뷰 내용

    private String summary;

    @Enumerated(EnumType.STRING)
    private ConfidenceType confidence;
}
