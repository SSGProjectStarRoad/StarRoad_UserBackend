package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ReviewReceiptDTO;
import com.ssg.starroad.review.entity.ReviewReceipt;
import com.ssg.starroad.review.repository.ReviewReceiptRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewReceiptServiceImplTest {

    @Autowired
    ReviewReceiptRepository reviewReceiptRepository;

    @Test
    void saveReviewReceipt() {
        // given
        ReviewReceiptDTO reviewReceiptDTO = ReviewReceiptDTO.builder()
                .shopName("test")
                .paymentType("card")
                .approvalNumber("1234")
                .purchaseDate(LocalDateTime.now())
                .build();

        ReviewReceipt reviewReceipt = reviewReceiptDTO.toEntity();

        // when
        ReviewReceipt savedReceipt = reviewReceiptRepository.save(reviewReceipt);

        // then
        Optional<ReviewReceipt> receipt = reviewReceiptRepository.findById(savedReceipt.getId());
        assertThat(receipt).isPresent();
        assertThat(receipt.get().getShopName()).isEqualTo("test");
        assertThat(receipt.get().getPaymentType()).isEqualTo("card");
        assertThat(receipt.get().getApprovalNumber()).isEqualTo("1234");
        assertThat(receipt.get().getPurchaseDate()).isEqualTo(reviewReceipt.getPurchaseDate());
    }

    @Test
    void getReviewReceiptById() {
    }

    @Test
    void getAllReviewReceipts() {
    }

    @Test
    void updateReviewReceipt() {
    }

    @Test
    void deleteReviewReceipt() {
    }
}