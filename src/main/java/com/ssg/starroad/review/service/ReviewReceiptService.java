package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewReceiptDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewReceiptService {

    // 리뷰 영수증 정보를 저장하고 저장된 정보를 DTO로 반환합니다.
    ReviewReceiptDTO saveReviewReceipt(ReviewReceiptDTO reviewReceiptDTO);

    // ID를 기반으로 리뷰 영수증 정보를 조회하고, 해당 정보를 DTO로 감싸서 반환합니다.
    Optional<ReviewReceiptDTO> getReviewReceiptById(Long id);

    // 모든 리뷰 영수증 정보를 조회하고, 그 정보들을 DTO 리스트로 반환합니다.
    List<ReviewReceiptDTO> getAllReviewReceipts();

    // 리뷰 영수증 정보를 업데이트합니다. ID와 업데이트 할 정보가 담긴 DTO를 매개변수로 받습니다.
    ReviewReceiptDTO updateReviewReceipt(Long id, ReviewReceiptDTO reviewReceiptDetails);

    // ID를 기반으로 리뷰 영수증 정보를 삭제합니다.
    void deleteReviewReceipt(Long id);

    ReviewReceiptDTO processAndSaveOcrResult(String ocrResultJson);
}
