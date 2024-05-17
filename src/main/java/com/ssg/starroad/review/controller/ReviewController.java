package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.DTO.ReviewReceiptDTO;
import com.ssg.starroad.review.DTO.ReviewRequestDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.service.ReviewReceiptService;
import com.ssg.starroad.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequestMapping("/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewReceiptService reviewReceiptService;


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
    @GetMapping // 클래스 레벨의 @RequestMapping의 경로(/reviews)를 사용
    public ResponseEntity<List<Review>> getAllReviews() {
        Review testReview = Review.builder()
                .contents("Test 입니다.")
                .build();
//        List<Review> reviews = reviewService.getAllReviews();
        List<Review> reviews = new ArrayList<>();
        reviews.add(testReview);
        return ResponseEntity.ok(reviews); // 조회된 리뷰 리스트를 반환
    }

}
