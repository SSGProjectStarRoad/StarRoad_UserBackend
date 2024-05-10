package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.entity.ReviewFeedback;

import java.util.List;

public interface ReviewFeedbackService {


    List<ReviewFeedbackDTO> getReviewFeedback(Long reviewId);
}