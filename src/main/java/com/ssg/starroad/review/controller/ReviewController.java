package com.ssg.starroad.review.controller;

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
import com.ssg.starroad.user.dto.RankUserDTO;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private final UserService userService;


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


    @GetMapping
    public ResponseEntity<ResponseReviewDTO> getAllReviews(@RequestParam String userEmail,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        try {
            ResponseReviewDTO responseReviewDTO = reviewService.findAllReview(userEmail, page, size);
            return ResponseEntity.ok(responseReviewDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/following")
    public ResponseEntity<ResponseReviewDTO> getFollowingReviews(@RequestParam String userEmail,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        System.out.printf("CReviewController::getFollowingReviews userEmail : %s\n", userEmail);
        try {
            System.out.printf("try문 실행");
            ResponseReviewDTO responseReviewDTO = reviewService.findFollowingReview(userEmail, page, size);
            return ResponseEntity.ok(responseReviewDTO);
        } catch (RuntimeException e) {
            System.out.printf(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/submit")
    public ResponseEntity<String> submitSurvey(
            @RequestPart("review") String reviewStr, // JSON 문자열로 받습니다.
            @RequestPart(value = "images", required = false) List<MultipartFile> uploadedImages) throws IOException {

        System.out.printf("UploadedImaged : " + uploadedImages);
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

        List<String> imageUrls = new ArrayList<>();
        if (uploadedImages != null && !uploadedImages.isEmpty()) {
            imageUrls = s3Uploader.upload(uploadedImages.toArray(new MultipartFile[uploadedImages.size()]), "ssg/reviews");
        }

        System.out.println("================================================================");
        for (String imageUrl : imageUrls) {
            System.out.println(imageUrl);
        }

        // surveyData를 합쳐서 ReviewFeedbackDTO 생성
        String combinedSurveyData = reviewDTO.getCombinedSurveyData();
        ReviewFeedbackDTO reviewFeedbackDTO = ReviewFeedbackDTO.builder()
                .reviewId(reviewId)
                .reviewFeedbackSelection(combinedSurveyData)
                .build();

        imageUrls.forEach(url -> reviewImageRepository.save(ReviewImage.builder()
                .review(savedReview)
                .imagePath(url)
                .build()));

        reviewFeedbackService.addReviewFeedback(reviewFeedbackDTO);

        return ResponseEntity.ok("설문이 성공적으로 제출되었습니다!");
    }

    @GetMapping("/rank")
    public ResponseEntity<List<RankUserDTO>> getRankUser(@RequestParam String userEmail) {
        List<RankUserDTO> rankUserDTOS = userService.getRankUser(userEmail);

        return ResponseEntity.ok(rankUserDTOS);
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<RankUserDTO>> getAllUser(@RequestParam String userEmail) {
        List<RankUserDTO> rankUserDTOS = userService.getAllUser(userEmail);

        return ResponseEntity.ok(rankUserDTOS);
    }

    @PostMapping("/addFollowUser")
    public ResponseEntity<String> getAllReviews(@RequestParam String userName,
                                                @RequestParam String userEmail
    ) {
        System.out.printf("CReviewController getAllReviews userName : %s, userEmail : %s", userName, userEmail);
        String followState = userService.addFollowUser(userName, userEmail);

        return ResponseEntity.ok("팔로우 성공");
    }
}

