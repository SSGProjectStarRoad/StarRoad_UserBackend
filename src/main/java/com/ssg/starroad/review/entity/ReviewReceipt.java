package com.ssg.starroad.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ReviewReceipt {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String shopName; // 매장 이름
    private String paymentType; // 결제 수단
    private String approvalNumber; // 승인 번호
    private LocalDateTime purchaseDate; // 구매 일자
}
