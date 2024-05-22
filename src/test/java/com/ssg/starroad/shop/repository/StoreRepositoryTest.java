package com.ssg.starroad.shop.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StoreRepositoryTest {



    @Autowired
    private StoreRepository storeRepository;

    private static final Logger log = LogManager.getLogger(StoreRepository.class);


    @Test
    public void testfindBystore() {
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.info(storeRepository.findById(1L));

    }

}