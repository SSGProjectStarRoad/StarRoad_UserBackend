package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.common.service.S3Uploader;
import com.ssg.starroad.review.DTO.ReviewFeedbackDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewFeedback;
import com.ssg.starroad.review.repository.ReviewFeedbackRepository;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewSelectionService;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewFeedbackServiceImpl implements ReviewFeedbackService {
    private final ReviewFeedbackRepository reviewFeedbackRepository;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;
    private final ReviewSelectionService reviewSelectionService;
    private final UserRepository userRepository;

    @Override
    public List<ReviewFeedbackDTO> getReviewFeedback(Long reviewId) {

        List<ReviewFeedback> reviewFeedbacks = reviewFeedbackRepository.findReviewFeedbackByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 데이터가 존재하지 않습니다"));
        List<ReviewFeedbackDTO> reviewFeedbackDTOList = reviewFeedbacks.stream()
                .map(ReviewFeedbackDTO::toReviewFeedbackDTO).collect(Collectors.toList());

        return reviewFeedbackDTOList;
    }

    @Transactional
    @Override
    public ReviewFeedbackDTO addReviewFeedback(ReviewFeedbackDTO reviewFeedbackDTO) {
        System.out.println("addReviewFeedBack 진입");
        Long userId = reviewFeedbackDTO.getId();
        User user = userRepository.findById(userId).get();

        Integer reviewExp = user.getReviewExp();
        Integer additionalPoint = reviewExp < 100 ? 100 : reviewExp < 200 ? 200 : 300;
        System.out.println("addReviewFeedBack User : " + user.toString());
        // 기존 User 엔티티를 업데이트합니다.
        user.setReviewExp(user.getReviewExp() + 10);
        user.setPoint(user.getPoint() + additionalPoint);

        // 변경된 User 엔티티를 저장합니다.
        userRepository.save(user);

        // reviewFeedbackSelection을 ','를 기준으로 나눠 String 배열에 담는다.
        String[] selections = reviewFeedbackDTO.getReviewFeedbackSelection().split(",");
        System.out.printf("selections : " + selections);
        for (String selection : selections) {
            System.out.printf("String : " + selection);
            selection = selection.replace("[", "").replace("]", "").replace("\"", "").trim();
            // ReviewFeedback 엔티티 생성 및 저장
            ReviewFeedback reviewFeedback = ReviewFeedback.builder()
                    .review(Review.builder().id(reviewFeedbackDTO.getReviewId()).build())
                    .reviewFeedbackSelection(selection.trim())
                    .build();

            reviewFeedbackRepository.save(reviewFeedback);
        }

        return reviewFeedbackDTO; // 저장된 ReviewFeedbackDTO 반환
    }

    @Transactional
    @Override
    public ReviewFeedbackDTO updateReviewFeedback(Long feedbackId, ReviewFeedbackDTO reviewFeedbackDTO) {
        ReviewFeedback reviewFeedback = reviewFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("피드백이 존재하지 않습니다."));

        reviewFeedback.changeReviewFeedbackSelection(reviewFeedbackDTO.getReviewFeedbackSelection());
        ReviewFeedback updatedFeedback = reviewFeedbackRepository.save(reviewFeedback);

        ReviewFeedbackDTO updatedDTO = new ReviewFeedbackDTO();
        updatedDTO.setId(updatedFeedback.getId());
        updatedDTO.setReviewId(updatedFeedback.getReview().getId());
        updatedDTO.setReviewFeedbackSelection(updatedFeedback.getReviewFeedbackSelection());

        return updatedDTO;
    }

    @Transactional
    @Override
    public void deleteReviewFeedback(Long feedbackId) {
        ReviewFeedback reviewFeedback = reviewFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("피드백이 존재하지 않습니다."));
        reviewFeedbackRepository.delete(reviewFeedback);
    }
}
