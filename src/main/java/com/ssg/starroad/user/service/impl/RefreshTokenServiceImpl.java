package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.common.util.TokenGenerator;
import com.ssg.starroad.user.entity.RefreshToken;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.RefreshTokenRepository;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // RefreshToken에서 직접적으로 userId를 접근할 필요 없이 관련된 User 객체를 통해 필요한 정보 얻을 수 있음

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    // 사용자가 로그인할 때마다 이 메소드를 호출하여
    // 사용자의 세션에 대한 유효성을 연장하기 위한 새로운 리프레시 토큰을 발급
    @Override
    public String createRefreshToken(Long userId) {
        // User 객체를 가져오는 로직
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 리프레시 토큰의 만료 시간 설정
        Instant expiryDate = Instant.now().plusSeconds(7 * 24 * 3600); // 7일 후 만료

        // 고유한 토큰 값을 생성
        String newToken = generateUniqueToken();

        // 기존의 리프레시 토큰이 있는지 확인
        Optional<RefreshToken> existingTokenOptional = refreshTokenRepository.findByUser_Id(userId);

        if (existingTokenOptional.isPresent()) {
            // 기존 토큰이 존재하면 업데이트
            RefreshToken existingToken = existingTokenOptional.get();
            existingToken.updateToken(newToken);
            existingToken.setExpiryDate(expiryDate);
            refreshTokenRepository.save(existingToken);
            return existingToken.getToken();
        } else {
            // 기존 토큰이 존재하지 않으면 새로 생성
            RefreshToken refreshToken = new RefreshToken(user, newToken, expiryDate);
            refreshTokenRepository.save(refreshToken);
            return refreshToken.getToken();
        }
    }

    // 고유한 토큰 값을 생성하는 데 사용. 특히 리프레시 토큰 내에서 고유성을 보장하는 데 사용
    // SecureRandom을 사용하여 예측이 불가능한 안전한 토큰을 생성
    // 이 토큰은 주로 리프레시 토큰의 일부로 사용되어 토큰 자체의 유일성을 보장
    public String generateUniqueToken() {
        return TokenGenerator.generateUniqueToken();
    }
}


