package com.ssg.starroad.shop.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StoreControllerTest {

    @Autowired
    private StoreController storeController;

    private static final Logger log = LogManager.getLogger(StoreControllerTest.class);
    @Test
    public void teststoreController() {
        storeController.StoreSearch();




    }
//    @Test
//    public void teststoreController2() {
//        log.info(
//                storeController.StoreGetWithReviewById(1L,"hklee@example.com",0,20,));
//    }

    @Test
    public void teststoreController3() {
        log.info(
                storeController.StoreGetGuidemapById(4L)
        );
    }


}