package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.DTO.ReviewReceiptDTO;
import com.ssg.starroad.review.service.ReviewReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review-receipts")
@RequiredArgsConstructor
public class ReviewReceiptController {

    private final ReviewReceiptService reviewReceiptService;

    // 리뷰 영수증 생성
    @PostMapping
    public ResponseEntity<ReviewReceiptDTO> createReviewReceipt(@RequestBody ReviewReceiptDTO reviewReceiptDTO) {
        ReviewReceiptDTO savedReviewReceipt = reviewReceiptService.saveReviewReceipt(reviewReceiptDTO);
        return ResponseEntity.ok(savedReviewReceipt);
    }

    // 리뷰 영수증 조회 (단일)
    @GetMapping("/{id}")
    public ResponseEntity<ReviewReceiptDTO> getReviewReceipt(@PathVariable Long id) {
        Optional<ReviewReceiptDTO> reviewReceiptDTOOptional = reviewReceiptService.getReviewReceiptById(id);

        // Optional 객체가 값을 가지고 있다면 그 값을 반환
        if (reviewReceiptDTOOptional.isPresent()) {
            return ResponseEntity.ok(reviewReceiptDTOOptional.get());
        } else {
            // 값이 없는 경우, 적절한 HTTP 상태 코드와 함께 응답을 반환할 수 있습니다.
            // 예를 들어, NOT_FOUND 상태 코드를 반환할 수 있습니다.
            return ResponseEntity.notFound().build();
        }
    }

    // 리뷰 영수증 전체 조회
    @GetMapping
    public ResponseEntity<List<ReviewReceiptDTO>> getAllReviewReceipts() {
        List<ReviewReceiptDTO> reviewReceipts = reviewReceiptService.getAllReviewReceipts();
        return ResponseEntity.ok(reviewReceipts);
    }

    // 리뷰 영수증 수정
    @PutMapping("/{id}")
    public ResponseEntity<ReviewReceiptDTO> updateReviewReceipt(@PathVariable Long id, @RequestBody ReviewReceiptDTO reviewReceiptDTO) {
        ReviewReceiptDTO updatedReviewReceipt = reviewReceiptService.updateReviewReceipt(id, reviewReceiptDTO);
        return ResponseEntity.ok(updatedReviewReceipt);
    }

    // 리뷰 영수증 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewReceipt(@PathVariable Long id) {
        reviewReceiptService.deleteReviewReceipt(id);
        return ResponseEntity.ok().build();
    }
}
