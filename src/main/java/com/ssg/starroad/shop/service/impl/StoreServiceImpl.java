package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.DTO.ReviewImageDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.repository.ReviewImageRepository;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewImageService;
import com.ssg.starroad.review.service.ReviewService;
import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.shop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
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
    private final ReviewService reviewService;

    @Override
    public List<StoreDTO> searchStoreList(Long id) {
        // 주어진 complexShoppingmallId로 매장 목록을 조회합니다.
        List<Store> stores = storeRepository.findByComplexShoppingmallId(id).orElseThrow(() -> new RuntimeException("존재하지 않는 종합 쇼핑몰입니다."));

        // 매장 엔티티 리스트를 StoreDTO 리스트로 변환합니다.
        List<StoreDTO> storeDTOList = stores.stream()
                .map(StoreDTO::toDTO).collect(Collectors.toList());

        return storeDTOList;
    }@Override
    public StoreWithReviewDTO findStoreWithReview(Long id, int pageNo, int pageSize) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 스토어입니다."));

        // 최신순으로 정렬된 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 정렬된 Pageable 객체를 사용하여 리뷰 페이지 가져오기
        Page<Review> reviewPage = reviewRepository.findAllWithPageByStoreId(id, pageable);

        Long totalReviewCount = reviewRepository.countByStoreId(id);

        // 각 선택지에 대한 피드백 개수를 조회합니다.
        long revisitCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(id, "재방문 하고 싶어요");
        long serviceSatisfactionCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(id, "서비스가 마음에 들어요");
        long reasonablePriceCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(id, "가격이 합리적입니다");
        long cleanlinessCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(id, "매장이 청결합니다");

        List<ReviewDTO> reviewDTOList = reviewPage.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                    List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = reviewService.countReviewsByUserId(review.getUser().getId());

                    return ReviewDTO.builder()
                            .id(review.getId())
                            .userId(review.getUser().getId())
                            .userNickname(review.getUser().getNickname())
                            .imagePath(review.getUser().getImagePath())
                            .storeId(review.getStore().getId())
                            .visible(review.isVisible())
                            .createDate(review.getCreatedAt())
                            .likeCount(review.getLikeCount())
                            .contents(review.getContents())
                            .summary(review.getSummary())
                            .confidence(review.getConfidence())
                            .reviewcount(userReviewCount)
                            .reviewImages(reviewImageDTOs)
                            .reviewFeedbacks(reviewFeedbackDTOs)
                            .build();
                })
                .collect(Collectors.toList());

        return StoreWithReviewDTO.builder()
                .id(store.getId())
                .contents(store.getContents())
                .operatingTime(store.getOperatingTime())
                .name(store.getName())
                .floor(store.getFloor().getFloor())
                .storeGuideMap(store.getStoreGuideMap())
                .contactNumber(store.getContactNumber())
                .imagePath(store.getImagePath())
                .storeType(store.getStoreType())
                .reviews(reviewDTOList)
                .pageNumber(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .hasNext(reviewPage.hasNext())
                .totalReviewCount(totalReviewCount)
                .revisitCount(revisitCount)
                .serviceSatisfactionCount(serviceSatisfactionCount)
                .reasonablePriceCount(reasonablePriceCount)
                .cleanlinessCount(cleanlinessCount)
                .build();
    }



    @Override
    public StoreDTO findStore(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않은 매장입니다"));

        StoreDTO storeDTO = StoreDTO.builder()
                .id(store.getId())
                .imagePath(store.getImagePath())
                .storeGuideMap(store.getStoreGuideMap())
                .name(store.getName())
                .floor(store.getFloor().getFloor())
                        .build();




        return storeDTO;
    }

    @Override
    // 매장 이름을 이용해 매장을 조회하고 매장 유형을 반환하는 메소드
    public String findStoreTypeByName(String name) {
        Store store = storeRepository.findByName(name).orElseThrow(() -> new RuntimeException("존재하지 않는 스토어입니다."));
        return store.getStoreType();
    }
}