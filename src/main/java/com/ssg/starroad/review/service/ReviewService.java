package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ResponseReviewDTO;
import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.entity.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Long countReviewsByUserId(Long userId);

    Review createReview(Review review);

    Optional<Review> getReviewById(Long id);

    List<Review> getAllReviews();

    Review updateReview(Long id, Review reviewDetails);

    void deleteReview(Long id);

    ResponseEntity<String> callOcrApi(MultipartFile imageFile) throws IOException;

    ResponseReviewDTO findAllReview(String userEmail, int pageNo, int pageSize);

    ResponseReviewDTO findFollowingReview(String userEmail, int pageNo, int pageSize);

    String makeSummary(String content);

    ResponseEntity<String> saveSurvey(ReviewDTO reviewDTO, List<MultipartFile> uploadedImages);

    ResponseReviewDTO getUserReview(String email,int pageNo, int pageSize);
}
