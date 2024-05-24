package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewKeywordDTO;
import java.util.Optional;

public interface ReviewKeywordService {

    /**
     * 리뷰 키워드를 생성하는 메소드.
     *
     * @param reviewKeywordDTO 리뷰 키워드 정보가 담긴 DTO
     * @return 생성된 리뷰 키워드의 DTO
     */
    ReviewKeywordDTO createReviewKeyword(ReviewKeywordDTO reviewKeywordDTO);

    /**
     * ID를 기반으로 리뷰 키워드를 찾는 메소드.
     *
     * @param id 찾고자 하는 리뷰 키워드의 ID
     * @return 찾은 리뷰 키워드의 DTO, 없을 경우 Optional.empty()
     */
    Optional<ReviewKeywordDTO> findReviewKeywordById(Long id);
}
