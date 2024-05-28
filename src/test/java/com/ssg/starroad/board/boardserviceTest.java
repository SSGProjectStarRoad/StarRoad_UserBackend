package com.ssg.starroad.board;

import com.ssg.starroad.board.service.BoardImageService;
import com.ssg.starroad.board.service.BoardService;
import com.ssg.starroad.coupon.repository.CouponRepositoryTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class boardserviceTest {
    private static final Logger log = LogManager.getLogger(CouponRepositoryTest.class);


    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardImageService boardImageService;

    @Test
    public void testBoardlist(){
        log.info(boardService.getEventBoards());
    }
    @Test
    public void testBoardImage(){
        log.info(boardImageService.getEventContent(1L));
    }
}
