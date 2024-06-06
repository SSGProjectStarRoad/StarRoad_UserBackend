package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.DTO.ReviewImageDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewKeyword;
import com.ssg.starroad.review.entity.ReviewSentiment;
import com.ssg.starroad.review.repository.*;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewImageService;
import com.ssg.starroad.review.service.ReviewService;
import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.shop.service.StoreService;
import com.ssg.starroad.user.repository.FollowRepository;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private final ReviewSentimentRepository reviewSentimentRepository;


    private final ReviewKeywordRepository reviewKeywordRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    @Override
    public List<ReviewKeyword> getKeywordsByStoreCategory(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("존재하지 않는 스토어입니다."));

        return reviewKeywordRepository.findAllByStoreType(store.getStoreType());
    }
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
    public StoreWithReviewDTO findStoreWithReview(Long storeId, String userEmail, int pageNo, int pageSize, String filter, String sort, String keyword) {

        // 주어진 storeId로 스토어를 조회합니다.
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("존재하지 않는 스토어입니다."));

        // 정렬 방식 설정
        Sort sortOption;
        if ("likes".equalsIgnoreCase(sort)) {
            sortOption = Sort.by(Sort.Direction.DESC, "likeCount");
        } else {
            sortOption = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        // 페이지 요청 객체를 생성합니다.
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortOption);

        // 스토어 ID와 필터 조건에 따라 리뷰 페이지를 조회합니다.
        Page<Review> reviewPage;
        if (filter == null || filter.isEmpty()) {
            if (keyword == null || keyword.isEmpty()) {
                // 필터와 키워드가 없으면 모든 리뷰를 조회합니다.
                reviewPage = reviewRepository.findAllWithPageByStoreId(storeId, pageable);
            } else {
                // 키워드가 있으면 Review의 contents 필드를 사용하여 리뷰를 조회합니다.
                reviewPage = reviewRepository.findByStoreIdAndContentsContaining(storeId, keyword, pageable);
            }
        } else {
            // 필터가 있으면 필터 조건에 맞는 리뷰만 조회합니다.
            reviewPage = reviewRepository.findAllWithPageByStoreIdAndReviewFeedbackSelection(storeId, filter, pageable);
        }

        // 스토어 ID로 전체 리뷰 개수를 조회합니다.
        Long totalReviewCount = reviewRepository.countByStoreId(storeId);

        // 특정 피드백을 받은 리뷰 개수를 조회합니다.
        long revisitCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(storeId, "재방문 하고 싶어요");
        long serviceSatisfactionCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(storeId, "서비스가 마음에 들어요");
        long reasonablePriceCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(storeId, "가격이 합리적입니다");
        long cleanlinessCount = reviewRepository.countByStoreIdAndReviewFeedbackSelection(storeId, "매장이 청결합니다");

        // 사용자 이메일을 통해 사용자 ID를 조회합니다.
        Long userId = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")).getId();

        List<ReviewDTO> reviewDTOList = reviewPage.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                    List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = reviewService.countReviewsByUserId(review.getUser().getId());
                    boolean isLiked = reviewLikeRepository.existsByUser_IdAndReview_Id(userId, review.getId());
                    boolean following = followRepository.existsByFromUserIdAndToUserId(userId, review.getUser().getId());

                    return ReviewDTO.builder()
                            .id(review.getId())
                            .following(following)
                            .userId(review.getUser().getId())
                            .userNickname(review.getUser().getNickname())
                            .userEmail(review.getUser().getEmail())
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
                            .isLiked(isLiked) // 로그인한 사용자가 좋아요를 눌렀는지 여부 추가
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


    public String findStoreName(String receiptShopName) {
        return storeRepository.findByName(receiptShopName)
                .map(Store::getName) // Store 객체에서 상점 이름을 추출
                .orElseThrow(() -> new RuntimeException("Store with name '" + receiptShopName + "' not found"));
    }

}