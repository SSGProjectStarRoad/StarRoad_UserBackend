package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.enums.ConfidenceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

import static com.ssg.starroad.shop.entity.QStore.store;
import static com.ssg.starroad.user.entity.QUser.user;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDTO {
    private Long id;
    private Long userId;
    private Long storeId;
    private boolean visible;
    private Long likeCount;
    private String paymentNum;
    private String contents;
    private String summary;
    private List<ReviewImageDTO> reviewImages; // 리뷰 이미지 리스트 추가
    private List<ReviewFeedbackDTO> reviewFeedbacks; // 리뷰 피드백 추가

    @Enumerated(EnumType.STRING)
    private ConfidenceType confidence;


}