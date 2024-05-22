package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ResponseReviewDTO;
import com.ssg.starroad.review.DTO.ReviewDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;
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

    ResponseReviewDTO findAllReview(int pageNo, int pageSize);

    ResponseReviewDTO findFollowingReview(Long id, int pageNo, int pageSize);
}
