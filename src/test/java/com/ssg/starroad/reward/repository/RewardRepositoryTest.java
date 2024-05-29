package com.ssg.starroad.reward.repository;



import com.ssg.starroad.coupon.repository.CouponRepositoryTest;
import com.ssg.starroad.reward.entity.Reward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RewardRepositoryTest {
    private static final Logger log = LogManager.getLogger(CouponRepositoryTest.class);

    @Autowired
    private RewardRepository rewardRepository;

//    @Test
//    public void testSave() {
//        Reward reward = new Reward(null,"세번째별");
//        rewardRepository.save(reward);
//    }
}
