package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.DTO.ReviewImageDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewImage;
import com.ssg.starroad.review.repository.ReviewImageRepository;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewImageService;
import com.ssg.starroad.reward.DTO.RewardDTO;
import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.shop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok 어노테이션으로 생성자 주입을 자동으로 해줍니다.
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository; // StoreRepository 인젝션
    private final ReviewRepository reviewRepository; // ReviewRepository 인젝션
    private final ReviewImageRepository reviewImageRepository; // ReviewImageRepository 인젝션
    private final ReviewImageService reviewImageService; // ReviewImageService 인젝션
    private final ReviewFeedbackService reviewFeedbackService;

    @Override
    public List<StoreDTO> searchStoreList(Long id) {
        // 주어진 complexShoppingmallId로 매장 목록을 조회합니다.
        List<Store> stores = storeRepository.findByComplexShoppingmallId(id).orElseThrow(() -> new RuntimeException("존재하지 않는 종합 쇼핑몰입니다."));

        // 매장 엔티티 리스트를 StoreDTO 리스트로 변환합니다.
        List<StoreDTO> storeDTOList = stores.stream()
                .map(StoreDTO::toDTO).collect(Collectors.toList());

        return storeDTOList;
    }

    @Override
    public StoreWithReviewDTO findStoreWithReview(Long id) {
        // 주어진 id로 매장 엔티티를 조회합니다.
        Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 스토어입니다."));
        // 주어진 id로 매장에 속한 리뷰 리스트를 조회합니다.
        List<Review> reviewList = reviewRepository.findAllByStoreId(id).orElse(Collections.emptyList());

        // 리뷰 엔티티 리스트를 ReviewDTO 리스트로 변환합니다. 이때 각 리뷰에 대한 이미지도 함께 가져옵니다.
        List<ReviewDTO> reviewDTOList = reviewList.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                   List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());

                    return ReviewDTO.builder()
                            .id(review.getId())
                            .userId(review.getUser().getId())
                            .storeId(review.getStore().getId())
                            .visible(review.isVisible())
                            .likeCount(review.getLikeCount())
                            .contents(review.getContents())
                            .summary(review.getSummary())
                            .confidence(review.getConfidence())
                            .reviewImages(reviewImageDTOs)
                            .reviewFeedbacks(reviewFeedbackDTOs)
                            .build();
                })
                .collect(Collectors.toList());

        // 매장 정보와 리뷰 정보를 포함한 StoreWithReviewDTO를 생성하여 반환합니다.
        return StoreWithReviewDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .floor(store.getFloor())
                .storeGuideMap(store.getStoreGuideMap())
                .contactNumber(store.getContactNumber())
                .imagePath(store.getImagePath())
                .storeType(store.getStoreType())
                .reviews(reviewDTOList)
                .build();
    }
}