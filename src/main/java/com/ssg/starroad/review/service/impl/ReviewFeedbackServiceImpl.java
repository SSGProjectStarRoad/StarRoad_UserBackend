package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.entity.ReviewFeedback;
import com.ssg.starroad.review.repository.ReviewFeedbackRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewFeedbackServiceImpl implements ReviewFeedbackService {
    private final ReviewFeedbackRepository reviewFeedbackRepository;

    @Override
    public List<ReviewFeedbackDTO> getReviewFeedback(Long reviewId) {

        List<ReviewFeedback> reviewFeedbacks = reviewFeedbackRepository.findReviewFeedbackByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 데이터가 존재하지 않습니다"));
        List<ReviewFeedbackDTO> reviewFeedbackDTOList = reviewFeedbacks.stream()
                .map(ReviewFeedbackDTO::toReviewFeedbackDTO).collect(Collectors.toList());

        return reviewFeedbackDTOList;
    }
}
