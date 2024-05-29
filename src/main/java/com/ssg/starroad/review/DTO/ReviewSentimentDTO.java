package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.enums.ConfidenceType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSentimentDTO {
    private Long id;
    private Long reviewId; // Review 엔티티의 ID
    private String content;
    private int totalOffset;
    private int totalLength;
    private ConfidenceType confidence;
    private int highlightOffset;
    private int highlightLength;
}
