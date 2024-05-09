package com.ssg.starroad.shop.DTO;

import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewImage;
import com.ssg.starroad.review.entity.ReviewSelection;
import com.ssg.starroad.shop.entity.Store;
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

    private String name;

    private int floor;


    private String storeGuideMap;


    private String contactNumber;


    private String imagePath;


    private String storeType;
}
