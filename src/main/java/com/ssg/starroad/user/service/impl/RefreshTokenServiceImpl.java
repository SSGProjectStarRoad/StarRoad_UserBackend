package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.entity.RefreshToken;
import com.ssg.starroad.user.repository.RefreshTokenRepository;
import com.ssg.starroad.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}

// RefreshToken에서 직접적으로 userId를 접근할 필요 없이 관련된 User 객체를 통해 필요한 정보 얻을 수 있음
