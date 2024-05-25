package com.ssg.starroad.user.repository;

import com.ssg.starroad.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser_Id(Long userId);

    Optional<RefreshToken> findByToken(String refreshToken);

    void deleteByUserEmail(String email);

    // 테스트 용
    @Transactional
    void deleteByUserId(Long userId);
}

// findByUser_Id(Long userId) 메서드를 사용하는 것은
// 일대일 관계를 통해 특정 사용자의 리프레시 토큰을 쉽게 찾기 위함
// JPA에서는 연관된 엔티티의 필드를 조회하는 방법으로
// _ (언더스코어)를 사용하여 엔티티의 필드 경로 지정 가능
// User 엔티티의 id 값을 이용하여 해당 User의 RefreshToken 조회 가능