package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.entity.ReviewFeedback;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ReviewFeedbackDTO {

    private Long id;
    private String reviewFeedbackSelection;
    private Long reviewId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SurveyData {
        private String essential;
        private String optional;
    }

    public static ReviewFeedbackDTO toReviewFeedbackDTO(ReviewFeedback entity) {
        return ReviewFeedbackDTO.builder()
                .id(entity.getId())
                .reviewId(entity.getReview().getId().longValue())
                .reviewFeedbackSelection(entity.getReviewFeedbackSelection())
                .build();
    }
}
