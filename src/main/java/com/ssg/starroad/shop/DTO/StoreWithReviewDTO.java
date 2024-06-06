package com.ssg.starroad.shop.DTO;

import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewImage;
import com.ssg.starroad.review.entity.ReviewSelection;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.enums.Floor;
import jakarta.persistence.Embedded;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class StoreWithReviewDTO {
    @Embedded
    private List<ReviewDTO> reviews;

    private Long id;
    private String contents;
    private String name;

    private String floor;

    private String operatingTime;
    private String storeGuideMap;

    private Long totalReviewCount;
    private String contactNumber;


    private String imagePath;

    private Long userId; // 로그인을 한 유저에 대한 필드
    private String storeType;
    //프로그래스바 관련필드
    private Long revisitCount;
    private Long serviceSatisfactionCount;
    private Long reasonablePriceCount;
    private Long cleanlinessCount;
    // 페이징 관련 필드
    private int pageNumber; // 현재 페이지 번호
    private int pageSize; // 페이지 크기
    private boolean hasNext; // 다음 페이지 존재 여부


}
