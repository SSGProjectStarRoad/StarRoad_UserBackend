package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.dto.ReviewRequestDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final RestTemplate restTemplate;

    @PostMapping("/write")
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequestDTO reviewRequest) {
        Review createdReview = reviewService.createReview(reviewRequest.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // OCR API를 호출하는 메서드 예시
    public ResponseEntity<String> callOcrApi(MultipartFile imageFile) throws IOException {
        // OCR API 엔드포인트
        String ocrApiUrl = "https://okn5z02skx.apigw.ntruss.com/custom/v1/30710/25e9f4b0de75101a14c45b2b4db7fc4ef23991aaabb3f2d5c40c7478127278f9/general";

        // OCR API에 요청을 보낼 때 필요한 headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", "aGtIcnlRZHZBRFF1cUNrUFFEWHpEWkRqUWVjTExndXk");

        // 이미지 파일을 Base64 인코딩
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());

        // 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("images", List.of(
                Map.of(
                        "format", "png",
                        "name", "medium",
                        "data", base64Image,
                        "url", "IMAGE_URL" // 이미지 URL이 필요한 경우
                )
        ));
        body.put("lang", "ko");
        body.put("requestId", "string");
        body.put("resultType", "string");
        body.put("timestamp", System.currentTimeMillis());
        body.put("version", "V1");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // OCR API에 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(ocrApiUrl, entity, String.class);

        return response;
    }

    // 이미지 파일을 받는 컨트롤러 메서드
    @PostMapping("/upload-and-ocr")
    public ResponseEntity<String> uploadAndOcr(@RequestParam("image") MultipartFile imageFile) throws IOException {
        // OCR API 호출
        ResponseEntity<String> ocrApiResponse = callOcrApi(imageFile);
        return ocrApiResponse;
    }

}
