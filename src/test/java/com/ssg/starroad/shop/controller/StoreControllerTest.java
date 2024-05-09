package com.ssg.starroad.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StoreControllerTest {

    @Autowired
    private StoreController storeController;

    @Test
    public void teststoreController() {
        storeController.StoreSearch();
    }

}