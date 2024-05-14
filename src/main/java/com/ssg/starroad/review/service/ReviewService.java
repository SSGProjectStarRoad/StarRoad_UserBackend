package com.ssg.starroad.review.service;

import com.ssg.starroad.review.entity.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review createReview(Review review);

    Optional<Review> getReviewById(Long id);

    List<Review> getAllReviews();

    Review updateReview(Long id, Review reviewDetails);

    void deleteReview(Long id);

    ResponseEntity<String> callOcrApi(MultipartFile imageFile) throws IOException;
}
