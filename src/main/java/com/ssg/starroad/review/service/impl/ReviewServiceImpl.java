package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ResponseReviewDTO;
import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.DTO.ReviewImageDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.repository.ReviewFollowRepository;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewImageService;
import com.ssg.starroad.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewFollowRepository ReviewFollowRepository;
    private final RestTemplate restTemplate;
    private final ReviewImageService reviewImageService; // ReviewImageService 인젝션
    private final ReviewFeedbackService reviewFeedbackService;
    private final ReviewFollowRepository reviewFollowRepository;

    @Override
    public Long countReviewsByUserId(Long userId) {
        return    reviewRepository.countByUserId(userId);
    }

    @Override
    @Transactional
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    @Transactional
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found for this id :: " + id));

        review.updateContents(reviewDetails.getContents());
        review.updateSummary(reviewDetails.getSummary());
        review.updateVisibility(reviewDetails.isVisible());
        review.updateLikeCount(reviewDetails.getLikeCount());

        final Review updatedReview = reviewRepository.save(review);
        return updatedReview;
    }


    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Review with id " + id + " not found"));
        reviewRepository.delete(review);
    }

    // OCR API를 호출하는 메서드 예시
    public ResponseEntity<String> callOcrApi(MultipartFile imageFile) throws IOException {
        // OCR API 엔드포인트
        String ocrApiUrl = "https://deqz3tj602.apigw.ntruss.com/custom/v1/30975/c4e5e9a47173ca497d023af6f322225681d8d3812a8648c4b32ba29bc7112a59/document/receipt";

        // OCR API에 요청을 보낼 때 필요한 headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", "b1NCaWFVeEpKb0lIbGF3c0xKaHlZS0FnY0tudERDZHQ=");

        // 이미지 파일을 Base64 인코딩
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());

        // 요청 바디 생성
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("format", "png");
        imageMap.put("data", base64Image);
        imageMap.put("name", "testReceipt");
//        imageMap.put("url", null); // null 값을 허용하는 HashMap 사용

        List<Map<String, Object>> images = new ArrayList<>();
        images.add(imageMap);

        Map<String, Object> body = new HashMap<>();
        body.put("version", "V2");
        body.put("requestId", "ocrCheck");
        body.put("timestamp", System.currentTimeMillis());
        body.put("images", images);
//        body.put("lang", "ko");
//        body.put("resultType", "string");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // OCR API에 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(ocrApiUrl, entity, String.class);
        System.out.println("callOcrApi : " + response);
        return response;
    }

    @Override
    public ResponseReviewDTO findAllReview(int pageNo, int pageSize) {
        // 최신순으로 정렬된 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        // 정렬된 Pageable 객체를 사용하여 리뷰 페이지 가져오기
        Page<Review> reviewPage = reviewRepository.findAll(pageable);

//        Long totalReviewCount = reviewRepository.countByStoreId(id);

        List<ReviewDTO> reviewDTOList = reviewPage.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                    List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = countReviewsByUserId(review.getUser().getId());

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
        return ResponseReviewDTO.builder()
                .reviews(reviewDTOList)
                .pageNumber(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .hasNext(reviewPage.hasNext())
                .build();
    }

    @Override
    public ResponseReviewDTO findFollowingReview(Long id, int pageNo, int pageSize) {
        // 최신순으로 정렬된 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        // 정렬된 Pageable 객체를 사용하여 리뷰 페이지 가져오기

        List<Long> toUserIds = reviewFollowRepository.findToUserIdsByFromUserId(id);

        Page<Review> reviewPage = reviewRepository.findAllByUserIds(toUserIds, pageable);

//        Page<Review> reviewPage = reviewFollowRepository.findToUserIdsByFromUserId(id).stream().map(followId -> reviewRepository.findAllWithPageByUserId(followId, pageable)).toList().stream().findFirst().orElse(null);

        List<ReviewDTO> reviewDTOList = reviewPage.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                    List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = countReviewsByUserId(review.getUser().getId());

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
        return ResponseReviewDTO.builder()
                .reviews(reviewDTOList)
                .pageNumber(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .hasNext(reviewPage.hasNext())
                .build();
    }
}
