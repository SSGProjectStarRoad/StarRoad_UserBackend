package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ReviewSentimentDTO;
import com.ssg.starroad.review.entity.ReviewSentiment;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.repository.ReviewSentimentRepository;
import com.ssg.starroad.review.service.ReviewSentimentService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ToString
public class ReviewSentimentServiceImpl implements ReviewSentimentService {
    private final ReviewSentimentRepository reviewSentimentRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewSentimentDTO createReviewSentiment(ReviewSentimentDTO reviewSentimentDTO) {
        ReviewSentiment reviewSentiment = ReviewSentiment.builder()
                .review(reviewRepository.findById(reviewSentimentDTO.getReviewId())
                        .orElseThrow(() -> new IllegalArgumentException("Review not found")))
                .content(reviewSentimentDTO.getContent())
                .totalOffset(reviewSentimentDTO.getTotalOffset())
                .totalLength(reviewSentimentDTO.getTotalLength())
                .confidence(reviewSentimentDTO.getConfidence())
                .highlightOffset(reviewSentimentDTO.getHighlightOffset())
                .highlightLength(reviewSentimentDTO.getHighlightLength())
                .build();

        reviewSentiment = reviewSentimentRepository.save(reviewSentiment);
        return convertToDTO(reviewSentiment);
    }

    // 간단한 조회 기능
    @Override
    public Optional<ReviewSentimentDTO> findById(Long id) {
        return reviewSentimentRepository.findById(id)
                .map(this::convertToDTO);
    }

    // 업데이트 기능
    @Override
    public ReviewSentimentDTO updateReviewSentiment(Long id, ReviewSentimentDTO reviewSentimentDTO) {
        ReviewSentiment reviewSentiment = reviewSentimentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReviewSentiment not found"));

        // 엔티티 업데이트 로직
        reviewSentiment.updateContent(reviewSentimentDTO.getContent());
        reviewSentiment.updateConfidence(reviewSentimentDTO.getConfidence());
        // 다른 필드도 필요에 따라 업데이트

        reviewSentiment = reviewSentimentRepository.save(reviewSentiment);
        return convertToDTO(reviewSentiment);
    }

    // 삭제 기능
    @Override
    public void deleteReviewSentiment(Long id) {
        reviewSentimentRepository.deleteById(id);
    }

    private ReviewSentimentDTO convertToDTO(ReviewSentiment reviewSentiment) {
        return ReviewSentimentDTO.builder()
                .id(reviewSentiment.getId())
                .reviewId(reviewSentiment.getReview().getId())
                .content(reviewSentiment.getContent())
                .totalOffset(reviewSentiment.getTotalOffset())
                .totalLength(reviewSentiment.getTotalLength())
                .confidence(reviewSentiment.getConfidence())
                .highlightOffset(reviewSentiment.getHighlightOffset())
                .highlightLength(reviewSentiment.getHighlightLength())
                .build();
    }
}
