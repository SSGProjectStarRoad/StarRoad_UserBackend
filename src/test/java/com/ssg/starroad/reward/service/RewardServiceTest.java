package com.ssg.starroad.reward.service;


import com.ssg.starroad.coupon.repository.CouponRepositoryTest;
import com.ssg.starroad.review.service.ReviewImageService;
import com.ssg.starroad.reward.DTO.RewardHistoryDTO;
import com.ssg.starroad.reward.DTO.RewardMemberDTO;
import com.ssg.starroad.reward.repository.RewardRepository;
import jdk.swing.interop.SwingInterOpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RewardServiceTest {

    private static final Logger log = LogManager.getLogger(CouponRepositoryTest.class);

    @Autowired
    private RewardService rewardService;

    @Autowired
    private RewardHistoryService rewardHistoryService;
    @Autowired
    private ReviewImageService reviewImageService;

//    @Test
//    public void test() {
//        System.out.println("@@@@@@@");
//        log.info(reviewImageService.getReviewImages(1L).toString());
//    }
//    @Test
//    public void testRewardList(){
//        log.info(rewardService.getRewardList());
//    }
//
//    @Test
//    public void testObtain(){
//        RewardMemberDTO rewardMemberDTO = new RewardMemberDTO(4L,3L);
//        rewardHistoryService.addReward(rewardMemberDTO);
//    }
//
//    @Test
//    public void testMyRewardList(){
//        log.info(rewardHistoryService.getRewardHistory(4L).toString());
//    }
}
