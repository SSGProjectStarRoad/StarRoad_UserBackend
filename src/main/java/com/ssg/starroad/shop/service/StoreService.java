package com.ssg.starroad.shop.service;

import com.ssg.starroad.review.entity.ReviewKeyword;
import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;

import java.util.List;

public interface StoreService {

    List<ReviewKeyword> getKeywordsByStoreCategory(Long storeId);

    List<StoreDTO> searchStoreList(Long id);

    StoreWithReviewDTO findStoreWithReview(Long storeId, String userEmail, int pageNo, int pageSize, String filter, String sort, String keyword);

    StoreDTO findStore(Long id);

    String findStoreTypeByName(String name);

    String findStoreName(String name);
}
