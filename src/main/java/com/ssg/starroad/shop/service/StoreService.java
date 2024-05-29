package com.ssg.starroad.shop.service;

import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;

import java.util.List;

public interface StoreService {


    List<StoreDTO> searchStoreList(Long id);

    StoreWithReviewDTO findStoreWithReview(Long storeId, String userEmail, int pageNo, int pageSize, String filter);

    StoreDTO findStore(Long id);

    String findStoreTypeByName(String name);

    String findStoreName(String receiptShopName);

}
