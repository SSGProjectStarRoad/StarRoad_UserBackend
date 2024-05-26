package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.service.ReviewImageService;
import com.ssg.starroad.review.service.ReviewLikeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewLikeServiceImplTest {


    @Autowired
    private ReviewLikeService reviewLikeService;

    private static final Logger log = LogManager.getLogger(ReviewLikeService.class);
    @Test
    @DisplayName("좋아요 누른 리뷰 체크하는테스트")
    public void test(){
        log.info("좋아요테스트");
        System.out.println("좋아요테스트");
        log.info(   reviewLikeService.getLikedReviewIdsByUserId(1L));
        log.info("!!!");
    }
}