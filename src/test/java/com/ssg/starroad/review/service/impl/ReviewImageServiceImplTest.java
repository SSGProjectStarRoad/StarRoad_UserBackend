package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.service.ReviewImageService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ReviewImageServiceImplTest {
@Autowired
private ReviewImageService reviewImageService;
    private static final Logger log = LogManager.getLogger(ReviewImageService.class);

    @Test
    void getReviewImage() {

        log.info(reviewImageService.getReviewImages(1L).toString());
    }
}