package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewFeedback;
import jakarta.persistence.Embedded;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewFeedbackDTO {

    @Embedded
    private List<Review> reviews;
    private Long id;
    private String reviewFeedbackSelection;
    private Long reviewId;

    public static ReviewFeedbackDTO toReviewFeedbackDTO(ReviewFeedback entity) {
        return ReviewFeedbackDTO.builder()
                .id(entity.getId())
                .reviewId(entity.getReview().getId().longValue())
                .reviewFeedbackSelection(entity.getReviewFeedbackSelection())
                .build();
    }




}
