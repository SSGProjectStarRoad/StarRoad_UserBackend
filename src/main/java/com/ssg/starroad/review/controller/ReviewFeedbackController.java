package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.DTO.ReviewSelectionDTO;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review-feedbacks")
@RequiredArgsConstructor
public class ReviewFeedbackController {
    private final ReviewFeedbackService reviewFeedbackService;


}
