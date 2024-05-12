package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewSentimentDTO;

import java.util.Optional;

public interface ReviewSentimentService {
    // 리뷰 감성 분석 데이터 생성
    ReviewSentimentDTO createReviewSentiment(ReviewSentimentDTO reviewSentimentDTO);

    // ID를 통한 리뷰 감성 분석 데이터 조회
    Optional<ReviewSentimentDTO> findById(Long id);

    // 리뷰 감성 분석 데이터 업데이트
    ReviewSentimentDTO updateReviewSentiment(Long id, ReviewSentimentDTO reviewSentimentDTO);

    // 리뷰 감성 분석 데이터 삭제
    void deleteReviewSentiment(Long id);
}
