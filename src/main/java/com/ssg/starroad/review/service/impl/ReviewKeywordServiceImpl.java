package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ReviewKeywordDTO;
import com.ssg.starroad.review.entity.ReviewKeyword;
import com.ssg.starroad.review.repository.ReviewKeywordRepository;
import com.ssg.starroad.review.service.ReviewKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewKeywordServiceImpl implements ReviewKeywordService {

    private final ReviewKeywordRepository reviewKeywordRepository;

    @Override
    public ReviewKeywordDTO createReviewKeyword(ReviewKeywordDTO reviewKeywordDTO) {
        ReviewKeyword reviewKeyword = ReviewKeyword.builder()
                .name(reviewKeywordDTO.getName())
                .build();

        reviewKeyword = reviewKeywordRepository.save(reviewKeyword);

        return ReviewKeywordDTO.builder()
                .id(reviewKeyword.getId())
                .name(reviewKeyword.getName())
                .build();
    }

    @Override
    public Optional<ReviewKeywordDTO> findReviewKeywordById(Long id) {
        return reviewKeywordRepository.findById(id)
                .map(keyword -> ReviewKeywordDTO.builder()
                        .id(keyword.getId())
                        .name(keyword.getName())
                        .build());
    }
}
