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

    // 이미지 저장
    public ReviewImageDTO saveReviewImage(ReviewImageDTO reviewImageDTO) {
        ReviewImage reviewImage = ReviewImage.builder()
                .imagePath(reviewImageDTO.getImagePath())
                // Review 엔티티 참조 필요 - 여기서는 간단화를 위해 생략
                .build();
        ReviewImage savedReviewImage = reviewImageRepository.save(reviewImage);
        return ReviewImageDTO.fromEntity(savedReviewImage);
    }

    // 하나의 리뷰에 해당하는 이미지 찾아오는 메소드
    @Override
    public List<ReviewImageDTO> getReviewImages(Long reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰 이미지를 찾을 수 없습니다."));
        return reviewImages.stream()
                .map(ReviewImageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 이미지 업데이트
    public ReviewImageDTO updateReviewImage(Long imageId, ReviewImageDTO reviewImageDTO) {
        ReviewImage reviewImage = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰 이미지를 찾을 수 없습니다."));
        reviewImage.updateImagePath(reviewImageDTO.getImagePath());
        // 필요한 경우 여기서 다른 필드도 업데이트
        ReviewImage updatedReviewImage = reviewImageRepository.save(reviewImage);
        return ReviewImageDTO.fromEntity(updatedReviewImage);
    }

    // 이미지 삭제
    public void deleteReviewImage(Long imageId) {
        ReviewImage reviewImage = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰 이미지를 찾을 수 없습니다."));
        reviewImageRepository.delete(reviewImage);
    }
}
