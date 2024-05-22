package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewSelectionDTO;

import java.util.List;

public interface ReviewSelectionService {
    ReviewSelectionDTO saveReviewSelection(ReviewSelectionDTO reviewSelectionDTO);
    ReviewSelectionDTO findReviewSelectionById(Long id);
    List<ReviewSelectionDTO> findAllReviewSelections();
    ReviewSelectionDTO updateReviewSelection(Long id, ReviewSelectionDTO reviewSelectionDto);
    void deleteReviewSelection(Long id);
    List<String> findReviewSelectionsByShopName(String shopName);
}
