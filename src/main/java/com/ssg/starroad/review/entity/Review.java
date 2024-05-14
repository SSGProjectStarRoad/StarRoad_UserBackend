package com.ssg.starroad.review.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.review.enums.ConfidenceType;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private boolean visible;

    private Long likeCount;

    private String paymentNum;

    private String contents;

    private String summary;

    @Enumerated(EnumType.STRING)
    private ConfidenceType confidence;

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public void updateSummary(String summary) {
        this.summary = summary;
    }

    public void updateVisibility(boolean visible) {
        this.visible = visible;
    }

    public void updateLikeCount(Long like) {
        if (likeCount == null) {
            likeCount = 0L; // null 값을 0으로 처리
        }
        else if (like < 0) {
            this.likeCount = 0L;
        }
        else {
            this.likeCount = like;
        }
    }

}
