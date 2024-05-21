package com.ssg.starroad.review.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.common.service.S3Uploader;
import com.ssg.starroad.review.DTO.*;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewImage;
import com.ssg.starroad.review.repository.ReviewImageRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewReceiptService;
import com.ssg.starroad.review.service.ReviewService;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewReceiptService reviewReceiptService;
    private final ReviewFeedbackService reviewFeedbackService;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final ReviewImageRepository imageRepository;
    private final ReviewImageRepository reviewImageRepository;


    @PostMapping("/write")
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequestDTO reviewRequest) {
        Review createdReview = reviewService.createReview(reviewRequest.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // 이미지 파일을 받는 컨트롤러 메서드
    @PostMapping("/upload-and-ocr")
    public ResponseEntity<ReviewReceiptDTO> uploadAndOcr(@RequestParam("image") MultipartFile imageFile) throws IOException {
        System.out.println("uploadAndOcr : 메소드 수신");
        // OCR API 호출
        ResponseEntity<String> ocrApiResponse = reviewService.callOcrApi(imageFile);
        System.out.println("uploadAndOcr" + ocrApiResponse);

        // 서비스를 통해 결과 처리
        ReviewReceiptDTO reviewReceiptDTO = reviewReceiptService.processAndSaveOcrResult(ocrApiResponse.getBody());

        return new ResponseEntity<>(reviewReceiptDTO, HttpStatus.OK);
    }

    // 특정 ID에 해당하는 리뷰 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 모든 리뷰 조회
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        // 서비스 계층에서 모든 리뷰를 조회
        List<Review> reviewList = reviewService.getAllReviews();

        // Review 엔티티를 ReviewDTO로 변환
        List<ReviewDTO> reviewDTOList = reviewList.stream().map(review -> {

//            List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
//            List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = reviewService.countReviewsByUserId(review.getUser().getId());

                    return ReviewDTO.builder()
                            .id(review.getId())
                            .userId(review.getUser().getId())
                            .userNickname(review.getUser().getNickname())
                            .storeId(review.getStore().getId())
                            .visible(review.isVisible())
                            .createDate(review.getCreatedAt())
                            .likeCount(review.getLikeCount())
                            .contents(review.getContents())
                            .summary(review.getSummary())
                            .confidence(review.getConfidence())
                            .reviewcount(userReviewCount)
//                    .reviewImages(reviewImageDTOs)
                            .build();
                })
                // createDate 기준으로 역순 정렬
                .sorted(Comparator.comparing(ReviewDTO::getCreateDate).reversed())
                .collect(Collectors.toList());

        // DTO 리스트를 ResponseEntity로 감싸서 반환
        return ResponseEntity.ok(reviewDTOList);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitSurvey(
            @RequestPart("review") String reviewStr, // JSON 문자열로 받습니다.
            @RequestPart("images") List<MultipartFile> uploadedImages) throws IOException {

        // JSON 문자열을 ReviewDTO 객체로 변환합니다.
        ReviewDTO reviewDTO = new ObjectMapper().readValue(reviewStr, ReviewDTO.class);

        System.out.println("reviewSelectionDTO toString : " + reviewDTO.toString());

        User user = userRepository.findBynickname(reviewDTO.getUserNickname()).orElseThrow(() -> new IllegalArgumentException("User not found for the nickname: " + reviewDTO.getUserNickname()));
        // Store 엔티티가 아직 저장되지 않은 경우 저장
        Store store = storeRepository.findByName(reviewDTO.getShopName()).orElseThrow(() -> new IllegalArgumentException("Store not found for the shop name: " + reviewDTO.getShopName()));
        System.out.printf("Store Id : " + store.getId());
        // Review 엔티티 생성 및 저장
        Review savedReview = reviewService.createReview(Review.builder()
                .user(user)
                .store(store)
                .paymentNum(reviewDTO.getPaymentNum())
                .contents(reviewDTO.getContents())
                .visible(true)
                .likeCount(0L)
                .build());
        Long reviewId = savedReview.getId();

        MultipartFile[] imagesArray = uploadedImages.toArray(new MultipartFile[0]);

        String imageUrls = s3Uploader.upload(imagesArray, "reviews");
        String url = imageUrls.split("\\[|\\]")[1];

        // surveyData를 합쳐서 ReviewFeedbackDTO 생성
        String combinedSurveyData = reviewDTO.getCombinedSurveyData();
        ReviewFeedbackDTO reviewFeedbackDTO = ReviewFeedbackDTO.builder()
                .reviewId(reviewId)
                .reviewFeedbackSelection(combinedSurveyData)
                .build();


        reviewImageRepository.save(ReviewImage.builder()
                        .review(savedReview)
                        .imagePath(url)
                .build());

        reviewFeedbackService.addReviewFeedback(reviewFeedbackDTO);

        return ResponseEntity.ok("설문이 성공적으로 제출되었습니다!");
    }

}

