package com.ssg.starroad.coupon.service.impl;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import com.ssg.starroad.coupon.repository.CouponRepository;
import com.ssg.starroad.coupon.repository.impl.CouponHistoryRepositoryCustom;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements CouponHistoryService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final CouponHistoryRepositoryCustom couponHistoryRepositoryCustom;

    @Override
    public void CouponUserAdd(Long userID, Long couponID) {
        CouponHistory couponHistory =
                new CouponHistory(null, userRepository.findById(userID).orElseThrow(),
                        couponID,false, LocalDate.now().plusDays(7));
        couponHistoryRepository.save(couponHistory);
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
            couponHistoryRepository.save(couponHistory); // 변경 사항 저장
            return true; // 성공적으로 업데이트 되었다면 true 반환
        }
        return false; // 쿠폰을 찾지 못했거나 이미 사용 중이라면 false 반환
    }

    @Override
    public List<CouponDTO> CouponsUserList(Long userID) {
        return couponHistoryRepositoryCustom.findCouponsByUserId(userID);
    }
}
