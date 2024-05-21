package com.ssg.starroad.shop.service;

import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;
import com.ssg.starroad.shop.entity.ComplexShoppingmall;
import com.ssg.starroad.shop.entity.Store;

import java.util.List;

public interface StoreService {

   List<StoreDTO> searchStoreList(Long id);
   StoreWithReviewDTO findStoreWithReview(Long id);
   StoreDTO findStore(Long id);
   String findStoreTypeByName(String name);
}
