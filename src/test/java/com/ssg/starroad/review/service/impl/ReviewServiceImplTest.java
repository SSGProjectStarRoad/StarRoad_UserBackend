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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    void getReviewByIdTest() {
        // 준비
        Long reviewId = 1L;
        Review review = Review.builder()
                .id(reviewId)
                .contents("테스트 리뷰 내용")
                .build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // 실행
        Optional<Review> foundReview = reviewService.getReviewById(reviewId);

        // 검증
        assertEquals(reviewId, foundReview.get().getId());
        assertEquals("테스트 리뷰 내용", foundReview.get().getContents());
        verify(reviewRepository).findById(reviewId);
    }


    @Test
    void getAllReviewsTest() {
        // 준비
        Review review1 = Review.builder()
                .contents("리뷰 내용 1")
                .build();
        Review review2 = Review.builder()
                .contents("리뷰 내용 2")
                .build();
        List<Review> reviewList = Arrays.asList(review1, review2);
        when(reviewRepository.findAll()).thenReturn(reviewList);

        // 실행
        List<Review> reviews = reviewService.getAllReviews();

        // 검증
        assertEquals(2, reviews.size());
        verify(reviewRepository).findAll();
    }


    @Test
    void updateReviewTest() {
        // 준비
        Long reviewId = 1L;
        Review existingReview = Review.builder()
                .id(reviewId)
                .contents("기존 내용")
                .build();

        Review updatedDetails = Review.builder()
                .contents("새로운 내용")
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(existingReview);

        // 실행
        Review updatedReview = reviewService.updateReview(reviewId, updatedDetails);

        // 검증
        assertEquals("새로운 내용", updatedReview.getContents());
        verify(reviewRepository).save(any(Review.class));
        verify(reviewRepository).findById(reviewId);
    }


    @Test
    void deleteReviewTest() {
        // 준비
        Long reviewId = 1L;
        Review review = Review.builder()
                .id(reviewId)
                .build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // 실행
        reviewService.deleteReview(reviewId);

        // 검증
        verify(reviewRepository).delete(review);
        verify(reviewRepository).findById(reviewId);
    }

}