package com.ssg.starroad.coupon.repository;

import com.querydsl.core.Tuple;
import com.ssg.starroad.coupon.entity.Coupon;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.repository.impl.CouponHistoryRepositoryCustom;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.Gender;
import com.ssg.starroad.user.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CouponRepositoryTest {

    private static final Logger log = LogManager.getLogger(CouponRepositoryTest.class);
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponHistoryService couponHistoryService;

    @Autowired
    private CouponHistoryRepository couponHistoryRepository;

    @Autowired
    private CouponHistoryRepositoryCustom couponHistoryRepositoryCustom;


    @Autowired
    private UserRepository userRepository;
}
//    @Test
//    public void testSaveCoupon() {
//        // 쿠폰 엔티티 생성
//        Coupon coupon = new Coupon(null, "Complex2", "Food", "Summer Sale", 1000, 10, 5000, 10000, "Active", LocalDate.now().plusDays(7));
//
//        // 쿠폰 저장
//        Coupon savedCoupon = couponRepository.save(coupon);
//
//        // 로그 출력
//        log.info("Saved Coupon: {}", savedCoupon);
//
//        // 검증
//        assertNotNull(savedCoupon);
//        assertNotNull(savedCoupon.getId());
//    }

//    @Test
//    public void testIssueCoupon() {
//        couponHistoryService.CouponUserAdd(4L, 3L);
//        log.info("check issue coupon: {}", couponHistoryRepository.findById(1L));
//    }
//}
//    @Test
//    public void testfindByUserId(){
////        Long userId = 1L;
//        Iterable<Tuple> Coupons = couponHistoryRepositoryCustom.findCouponsByUserId(userId);

//        // couponHistories를 반복하여 각 객체의 정보를 로그에 출력
//        for (Coupon Coupon : Coupons) {
//            log.info("Coupon History: {}", Coupon);
//        }
//    }
//    @Test
//    public void testSaveUser(){
//        // User 엔티티 생성
//        User user = new User(
//                null,  // ID는 데이터베이스에 의해 자동 생성되므로 null을 전달
//                "securepassword",  // bcrypt로 인코딩된 비밀번호 가정
//                "John Doe",
//                "hriver",
//                null,  // imagePath, 현재 null로 설정, 필요에 따라 수정
//                Gender.MALE,
//                LocalDate.of(1990, 1, 1),
//                "010-1234-5678",
//                "river.doe@example.com",
//                "local1",
//                0,  // reviewExp
//                100, // point
//                ActiveStatus.ACTIVE
//        );
//
//        // User 저장
//        User savedUser = userRepository.save(user);
//        log.info("Saved User: {}", savedUser);
//    }
