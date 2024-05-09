package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ReviewFeedbackServiceImplTest {



    @Autowired
    private ReviewFeedbackService reviewFeedbackService;
    private static final Logger log = LogManager.getLogger(ReviewFeedbackService.class);
    @Test
    void findTestReviewFeedback() {
        log.info(reviewFeedbackService.getReviewFeedback(2L));
        log.info("태스트 성공");
    }
}