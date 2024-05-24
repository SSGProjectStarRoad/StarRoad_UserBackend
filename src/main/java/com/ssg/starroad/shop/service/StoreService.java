package com.ssg.starroad.shop.service;

import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;
import com.ssg.starroad.shop.entity.ComplexShoppingmall;
import com.ssg.starroad.shop.entity.Store;

import java.util.List;

public interface StoreService {

   List<StoreDTO> searchStoreList(Long id);
   StoreWithReviewDTO findStoreWithReview(Long storeId,String userEmail, int pageNo,int pageSize);
   StoreDTO findStore(Long id);
}
