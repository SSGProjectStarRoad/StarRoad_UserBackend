package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Long countReviewsByUserId(Long userId) {
        return    reviewRepository.countByUserId(userId);
    }

    @Override
    @Transactional
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    @Transactional
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found for this id :: " + id));

        review.updateContents(reviewDetails.getContents());
        review.updateSummary(reviewDetails.getSummary());
        review.updateVisibility(reviewDetails.isVisible());
        review.updateLikeCount(reviewDetails.getLikeCount());

        final Review updatedReview = reviewRepository.save(review);
        return updatedReview;
    }


    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Review with id " + id + " not found"));
        reviewRepository.delete(review);
    }
}
