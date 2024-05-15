package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewImage;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewImageDTO {

    private Long id;
    private Long reviewId;
    private String imagePath;


    @Override
    public String toString() {
        return "ReviewImageDTO{" +
                "id=" + id +
                ", reviewId=" + reviewId +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    public static ReviewImageDTO toDTO(ReviewImage entity) {
        return ReviewImageDTO.builder()
                .id(entity.getId())
                .reviewId(entity.getReview().getId().longValue())
                .imagePath(entity.getImagePath())
                .build();
    }

    public static ReviewImageDTO fromEntity(ReviewImage reviewImage) {
        return ReviewImageDTO.builder()
                .id(reviewImage.getId())
                .reviewId(reviewImage.getReview().getId())
                .imagePath(reviewImage.getImagePath())
                .build();
    }
}
