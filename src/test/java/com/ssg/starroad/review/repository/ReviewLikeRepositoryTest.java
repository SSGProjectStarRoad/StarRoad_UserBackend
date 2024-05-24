package com.ssg.starroad.review.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ReviewLikeRepositoryTest {


    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Test
    void booleanteest() {
        assertThat(reviewLikeRepository.existsByUser_IdAndReview_Id(1L,43L)).isTrue();
    }
}