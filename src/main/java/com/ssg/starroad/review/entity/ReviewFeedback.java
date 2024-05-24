package com.ssg.starroad.review.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Embeddable
@Builder
@AllArgsConstructor
public class ReviewFeedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Column
    private String reviewFeedbackSelection;

    // reviewFeedbackSelection 필드의 값을 변경하는 메서드
    public void changeReviewFeedbackSelection(String newSelection) {
        // 필요한 경우, 여기에 상태 변경 전후에 대한 검증 로직을 추가할 수 있습니다.
        this.reviewFeedbackSelection = newSelection;
    }
}
