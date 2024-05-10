package com.ssg.starroad.review.DTO;

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
}
