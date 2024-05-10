package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ReviewImageDTO;
import com.ssg.starroad.review.entity.ReviewImage;
import com.ssg.starroad.review.repository.ReviewImageRepository;
import com.ssg.starroad.review.service.ReviewImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {
    private final ReviewImageRepository reviewImageRepository;

    // 하나의 리뷰에 해당하는 이미지 찾아오는 메소드
    @Override
    public List<ReviewImageDTO> getReviewImages(Long reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 데이터가 존재하지 않습니다"));
        List<ReviewImageDTO> reviewImageDTOS = reviewImages.stream()
                .map(ReviewImageDTO::toDTO).collect(Collectors.toList());
        return reviewImageDTOS;


    }
}