package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void createReviewTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 준비
//        Review review = new Review();
        Constructor<Review> constructor = Review.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Review review = constructor.newInstance();
        review.updateContents("테스트 리뷰 내용");
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // 실행
        Review createdReview = reviewService.createReview(review);

        // 검증
        assertEquals(review.getContents(), createdReview.getContents());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void getReviewById() {
    }

    @Test
    void getAllReviews() {
    }

    @Test
    void updateReview() {
    }

    @Test
    void deleteReview() {
    }
}