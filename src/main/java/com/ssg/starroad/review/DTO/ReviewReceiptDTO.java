package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.entity.ReviewReceipt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReviewReceiptDTO {
    private String shopName;
    private String paymentType;
    private String approvalNumber;
    private LocalDateTime purchaseDate;

    public ReviewReceipt toEntity() {
        return ReviewReceipt.builder()
                .shopName(this.shopName)
                .paymentType(this.paymentType)
                .approvalNumber(this.approvalNumber)
                .purchaseDate(this.purchaseDate)
                .build();
    }
}
