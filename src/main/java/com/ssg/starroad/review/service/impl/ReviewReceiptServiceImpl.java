package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.dto.ReviewReceiptDTO;
import com.ssg.starroad.review.entity.ReviewReceipt;
import com.ssg.starroad.review.repository.ReviewReceiptRepository;
import com.ssg.starroad.review.service.ReviewReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewReceiptServiceImpl implements ReviewReceiptService {

    private final ReviewReceiptRepository reviewReceiptRepository;

    @Override
    public ReviewReceiptDTO saveReviewReceipt(ReviewReceiptDTO reviewReceiptDTO) {
        // DTO를 엔티티로 변환
        ReviewReceipt reviewReceipt = ReviewReceipt.builder()
                .shopName(reviewReceiptDTO.getShopName())
                .paymentType(reviewReceiptDTO.getPaymentType())
                .approvalNumber(reviewReceiptDTO.getApprovalNumber())
                .purchaseDate(reviewReceiptDTO.getPurchaseDate())
                .build();

        // 데이터 저장
        ReviewReceipt savedReviewReceipt = reviewReceiptRepository.save(reviewReceipt);

        // 저장된 엔티티를 다시 DTO로 변환하여 반환
        return new ReviewReceiptDTO(
                savedReviewReceipt.getShopName(),
                savedReviewReceipt.getPaymentType(),
                savedReviewReceipt.getApprovalNumber(),
                savedReviewReceipt.getPurchaseDate()
        );
    }

    @Override
    public Optional<ReviewReceiptDTO> getReviewReceiptById(Long id) {
        return reviewReceiptRepository.findById(id)
                .map(reviewReceipt -> new ReviewReceiptDTO(
                        reviewReceipt.getShopName(),
                        reviewReceipt.getPaymentType(),
                        reviewReceipt.getApprovalNumber(),
                        reviewReceipt.getPurchaseDate()
                ));
    }

    @Override
    public List<ReviewReceiptDTO> getAllReviewReceipts() {
        return reviewReceiptRepository.findAll().stream()
                .map(reviewReceipt -> new ReviewReceiptDTO(
                        reviewReceipt.getShopName(),
                        reviewReceipt.getPaymentType(),
                        reviewReceipt.getApprovalNumber(),
                        reviewReceipt.getPurchaseDate()
                )).collect(Collectors.toList());
    }

    @Override
    public ReviewReceiptDTO updateReviewReceipt(Long id, ReviewReceiptDTO reviewReceiptDetailsDTO) {
        ReviewReceipt reviewReceipt = reviewReceiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReviewReceipt not found for this id :: " + id));

        reviewReceipt.setShopName(reviewReceiptDetailsDTO.getShopName());
        reviewReceipt.setPaymentType(reviewReceiptDetailsDTO.getPaymentType());
        reviewReceipt.setApprovalNumber(reviewReceiptDetailsDTO.getApprovalNumber());
        reviewReceipt.setPurchaseDate(reviewReceiptDetailsDTO.getPurchaseDate());

        ReviewReceipt updatedReviewReceipt = reviewReceiptRepository.save(reviewReceipt);

        return new ReviewReceiptDTO(
                updatedReviewReceipt.getShopName(),
                updatedReviewReceipt.getPaymentType(),
                updatedReviewReceipt.getApprovalNumber(),
                updatedReviewReceipt.getPurchaseDate()
        );
    }

    @Override
    public void deleteReviewReceipt(Long id) {
        ReviewReceipt reviewReceipt = reviewReceiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReviewReceipt not found for this id :: " + id));
        reviewReceiptRepository.delete(reviewReceipt);
    }
}
