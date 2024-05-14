package com.ssg.starroad.review.service;

import com.ssg.starroad.review.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Long countReviewsByUserId(Long userId);
  
    Review createReview(Review review);

    Optional<Review> getReviewById(Long id);

    List<Review> getAllReviews();

    Review updateReview(Long id, Review reviewDetails);

    void deleteReview(Long id);
}
