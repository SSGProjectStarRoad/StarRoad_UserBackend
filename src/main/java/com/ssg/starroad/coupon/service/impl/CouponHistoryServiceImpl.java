package com.ssg.starroad.coupon.service.impl;

import com.ssg.starroad.coupon.DTO.CouponDTO;
import com.ssg.starroad.coupon.entity.Coupon;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import com.ssg.starroad.coupon.repository.CouponRepository;
import com.ssg.starroad.coupon.repository.impl.CouponHistoryRepositoryCustom;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import com.ssg.starroad.reward.entity.RewardProcess;
import com.ssg.starroad.reward.repository.RewardProcessRepository;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements CouponHistoryService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final CouponHistoryRepositoryCustom couponHistoryRepositoryCustom;
    private final RewardProcessRepository rewardProcessRepository;

    @Override
    public int CouponUserAdd(String email, Long couponID) {
        Coupon coupon = couponRepository.findById(couponID).orElseThrow();
        int max = coupon.getMaxAmount();
        int min = coupon.getMinAmount();
        int rate = ThreadLocalRandom.current().nextInt(min, max + 1);

        CouponHistory couponHistory =
                new CouponHistory(null, userRepository.findByEmail(email).orElseThrow(),
                        couponID,false, LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)),rate);
        couponHistoryRepository.save(couponHistory);

        Long userID = userRepository.findByEmail(email).orElseThrow().getId();
        RewardProcess rewardProcess =rewardProcessRepository.findById(userID).orElseThrow();
        rewardProcess.setIssueStatus(true);
        rewardProcessRepository.save(rewardProcess);
        return rate;
    }

    @Override
    public CouponDTO CouponDetails(Long couponID) {
        return   modelMapper.map(couponRepository.findById(couponID),CouponDTO.class);
    }

    @Override
    public boolean CouponUsageModify(Long couponHistoryId) {

        // 쿠폰 조회
        Optional<CouponHistory> optionalCouponHistory = couponHistoryRepository.findById(couponHistoryId);
        if (optionalCouponHistory.isPresent()) {
            CouponHistory couponHistory = optionalCouponHistory.get();
            couponHistory.useCoupon(); // 상태를 true로 변경
            Long userid = couponHistory.getUser().getId();
            RewardProcess rewardProcess = rewardProcessRepository.findById(userid).orElseThrow();
            rewardProcess.setUsageStatus(true);
            rewardProcess.setCouponCount(rewardProcess.getCouponCount()+1);
            rewardProcessRepository.save(rewardProcess);
            couponHistoryRepository.save(couponHistory); // 변경 사항 저장
            return true; // 성공적으로 업데이트 되었다면 true 반환
        }
        return false; // 쿠폰을 찾지 못했거나 이미 사용 중이라면 false 반환
    }

    @Override
    public List<CouponDTO> CouponsUserList(String email) {
        Long userID = userRepository.findByEmail(email).orElseThrow().getId();
        List<CouponDTO> coupons = couponHistoryRepositoryCustom.findCouponsByUserId(userID);
        LocalDate today = LocalDate.now();

        coupons.forEach(coupon -> {
            if (coupon.getCouponExpiredAt().isBefore(today)) {
                coupon.setCouponUsageStatus(true);
                CouponHistory couponHistory=couponHistoryRepository.findById(coupon.getCouponHistoryId()).orElseThrow();
               couponHistory.setUsageStatus(true);
                couponHistoryRepository.save(couponHistory);  // 변경된 쿠폰 상태 저장
            }
        });

        return coupons.stream()
                .sorted(Comparator.comparing(CouponDTO::isCouponUsageStatus)
                        .thenComparing(CouponDTO::getCouponExpiredAt, Comparator.reverseOrder())) // 날짜가 최신순으로 정렬
                .collect(Collectors.toList());
    }
}
