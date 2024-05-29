package com.ssg.starroad.review.entity;

import com.ssg.starroad.review.enums.ConfidenceType;
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
@AllArgsConstructor
@Builder
public class ReviewSentiment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private String content;
    private int totalOffset;
    private int totalLength;

    @Enumerated(EnumType.STRING)
    private ConfidenceType confidence;

    private int highlightOffset;
    private int highlightLength;

    // 엔티티의 content 필드 업데이트를 위한 메소드
    public void updateContent(String content) {
        this.content = content;
    }

    // 엔티티의 confidence 필드 업데이트를 위한 메소드
    public void updateConfidence(ConfidenceType confidence) {
        this.confidence = confidence;
    }
}
