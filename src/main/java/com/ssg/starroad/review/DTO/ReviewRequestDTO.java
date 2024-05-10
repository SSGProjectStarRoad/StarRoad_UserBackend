package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private User user;

    private Store store;

    private boolean visible;

    private Long likeCount;

    private String paymentNum;

    private String contents;

    public Review toEntity() {
        return Review.builder()
                .user(user)
                .store(store)
                .paymentNum(paymentNum)
                .contents(contents)
        .build();
    }

}
