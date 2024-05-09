package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewImageDTO;
import com.ssg.starroad.review.entity.ReviewImage;

import java.util.List;

public interface ReviewImageService {

    List<ReviewImageDTO> getReviewImages(Long reviewId);
}
