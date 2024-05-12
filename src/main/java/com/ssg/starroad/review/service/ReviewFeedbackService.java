package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;

import java.util.List;

public interface ReviewFeedbackService {

    // 특정 리뷰에 대한 피드백 조회
    List<ReviewFeedbackDTO> getReviewFeedback(Long reviewId);

    // 피드백 추가
    ReviewFeedbackDTO addReviewFeedback(ReviewFeedbackDTO reviewFeedbackDTO);

    // 피드백 수정
    ReviewFeedbackDTO updateReviewFeedback(Long feedbackId, ReviewFeedbackDTO reviewFeedbackDTO);

    // 피드백 삭제
    void deleteReviewFeedback(Long feedbackId);
}