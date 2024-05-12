package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.DTO.ReviewSelectionDTO;
import com.ssg.starroad.review.entity.ReviewSelection;
import com.ssg.starroad.review.repository.ReviewSelectionRepository;
import com.ssg.starroad.review.service.ReviewSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewSelectionServiceImpl implements ReviewSelectionService {

    private final ReviewSelectionRepository reviewSelectionRepository;

    // 저장
    public ReviewSelectionDTO saveReviewSelection(ReviewSelectionDTO reviewSelectionDto) {
        ReviewSelection reviewSelection = ReviewSelection.builder()
                .shopType(reviewSelectionDto.getShopType())
                .content(reviewSelectionDto.getContent())
                .build();

        ReviewSelection savedEntity = reviewSelectionRepository.save(reviewSelection);
        return ReviewSelectionDTO.fromEntity(savedEntity);
    }

    // 조회 (단일 ID)
    public ReviewSelectionDTO findReviewSelectionById(Long id) {
        return reviewSelectionRepository.findById(id)
                .map(ReviewSelectionDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("ReviewSelection not found for the id: " + id));
    }

    // 조회 (전체 리스트)
    public List<ReviewSelectionDTO> findAllReviewSelections() {
        return reviewSelectionRepository.findAll()
                .stream()
                .map(ReviewSelectionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 수정
    public ReviewSelectionDTO updateReviewSelection(Long id, ReviewSelectionDTO reviewSelectionDto) {
        ReviewSelection existingReviewSelection = reviewSelectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReviewSelection not found for the id: " + id));

        ReviewSelection reviewSelectionToUpdate = ReviewSelection.builder()
                .id(existingReviewSelection.getId()) // 기존 ID 유지
                .shopType(reviewSelectionDto.getShopType())
                .content(reviewSelectionDto.getContent())
                .build();

        ReviewSelection updatedEntity = reviewSelectionRepository.save(reviewSelectionToUpdate);
        return ReviewSelectionDTO.fromEntity(updatedEntity);
    }

    // 삭제
    public void deleteReviewSelection(Long id) {
        ReviewSelection reviewSelection = reviewSelectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReviewSelection not found for the id: " + id));
        reviewSelectionRepository.delete(reviewSelection);
    }
}
