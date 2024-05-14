package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewFeedback;
import com.ssg.starroad.review.repository.ReviewFeedbackRepository;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewFeedbackServiceImpl implements ReviewFeedbackService {
    private final ReviewFeedbackRepository reviewFeedbackRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewFeedbackDTO> getReviewFeedback(Long reviewId) {

        List<ReviewFeedback> reviewFeedbacks = reviewFeedbackRepository.findReviewFeedbackByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 데이터가 존재하지 않습니다"));
        List<ReviewFeedbackDTO> reviewFeedbackDTOList = reviewFeedbacks.stream()
                .map(ReviewFeedbackDTO::toReviewFeedbackDTO).collect(Collectors.toList());

        return reviewFeedbackDTOList;
    }

    @Transactional
    @Override
    public ReviewFeedbackDTO addReviewFeedback(ReviewFeedbackDTO reviewFeedbackDTO) {
        // Review 찾기
        Review review = reviewRepository.findById(reviewFeedbackDTO.getReviewId())
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

        // DTO에서 엔티티로 변환 (실제 구현시 Review 엔티티 참조 필요)
        ReviewFeedback reviewFeedback = ReviewFeedback.builder()
                .reviewFeedbackSelection(reviewFeedbackDTO.getReviewFeedbackSelection())
                .review(review)
                .build();

        ReviewFeedback savedFeedback = reviewFeedbackRepository.save(reviewFeedback);

        // 엔티티에서 DTO로 변환
        ReviewFeedbackDTO savedDTO = new ReviewFeedbackDTO();
        savedDTO.setId(savedFeedback.getId());
        savedDTO.setReviewId(savedFeedback.getReview().getId());
        savedDTO.setReviewFeedbackSelection(savedFeedback.getReviewFeedbackSelection());

        return savedDTO;
    }

    @Transactional
    @Override
    public ReviewFeedbackDTO updateReviewFeedback(Long feedbackId, ReviewFeedbackDTO reviewFeedbackDTO) {
        ReviewFeedback reviewFeedback = reviewFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("피드백이 존재하지 않습니다."));

        reviewFeedback.changeReviewFeedbackSelection(reviewFeedbackDTO.getReviewFeedbackSelection());
        ReviewFeedback updatedFeedback = reviewFeedbackRepository.save(reviewFeedback);

        ReviewFeedbackDTO updatedDTO = new ReviewFeedbackDTO();
        updatedDTO.setId(updatedFeedback.getId());
        updatedDTO.setReviewId(updatedFeedback.getReview().getId());
        updatedDTO.setReviewFeedbackSelection(updatedFeedback.getReviewFeedbackSelection());

        return updatedDTO;
    }

    @Transactional
    @Override
    public void deleteReviewFeedback(Long feedbackId) {
        ReviewFeedback reviewFeedback = reviewFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("피드백이 존재하지 않습니다."));
        reviewFeedbackRepository.delete(reviewFeedback);
    }
}
