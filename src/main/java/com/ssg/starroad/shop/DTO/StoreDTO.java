package com.ssg.starroad.shop.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewImage;
import com.ssg.starroad.review.entity.ReviewSelection;
import com.ssg.starroad.shop.entity.Store;
import jakarta.persistence.Embedded;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StoreDTO {

    private String name;

    private int floor;


    private String storeGuideMap;


    private String contactNumber;

    private String imagePath;


    private String storeType;

    public static StoreDTO toDTO(Store entity) {
        return StoreDTO.builder()
                .name(entity.getName())
                .floor(entity.getFloor())
                .storeGuideMap(entity.getStoreGuideMap())
                .contactNumber(entity.getContactNumber())
                .imagePath(entity.getImagePath())
                .storeType(entity.getStoreType())
                .build();
    }


}

