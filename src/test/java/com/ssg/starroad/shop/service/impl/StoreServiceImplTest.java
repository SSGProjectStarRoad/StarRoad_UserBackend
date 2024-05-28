package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.review.service.ReviewService;
import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.service.StoreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreServiceImplTest {

    @Autowired
    private StoreService storeService;


    @Autowired
    private ReviewService reviewService;
    private static final Logger log = LogManager.getLogger(StoreServiceImplTest.class);


    @Test
    @DisplayName("스토어리스트뽑는 테스트")
    void getStore() {


        log.info("dafsdf");
        List<StoreDTO> stores = storeService.searchStoreList(1L);

//        assertEquals(10, stores.size());
        log.info(stores.toString());
    }

//    @Test
//    @DisplayName("Store단일 매장 전체 리뷰 작성메소드 테스트")
//    void StoreWithAllReviewTest() {
//        log.info(storeService.findStoreWithReview(2L,"hklee@example.com",1,20));
//
//    }




    @Test
    void StoreWithGuideMap(){
        log.info(storeService.findStore(508L));
    }
}