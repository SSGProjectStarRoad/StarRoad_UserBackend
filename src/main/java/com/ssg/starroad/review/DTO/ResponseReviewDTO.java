package com.ssg.starroad.review.DTO;

import jakarta.persistence.Embedded;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class ResponseReviewDTO {
    @Embedded
    private List<ReviewDTO> reviews;
    // 페이징 관련 필드
    private int pageNumber; // 현재 페이지 번호
    private int pageSize; // 페이지 크기
    private boolean hasNext; // 다음 페이지 존재 여부
}
