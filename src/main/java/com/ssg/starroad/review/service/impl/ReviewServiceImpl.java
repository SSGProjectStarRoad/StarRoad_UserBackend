package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {


    private final ReviewRepository reviewRepository;

    @Override
    public Long countReviewsByUserId(Long userId) {


        return    reviewRepository.countByUserId(userId);
    }
}
