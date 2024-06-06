package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.service.RefreshTokenService;
import com.ssg.starroad.user.service.TokenService;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

// RefreshToken 엔티티는 User 엔티티와 일대일 관계
// user 필드를 통해 User 엔티티를 참조
// userId를 직접 가져오는 대신, User 객체를 통해 ID를 가져와야
    @Override
    public String createNewAccessToken(String refreshToken) {

        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token");
        }

        // RefreshToken 객체를 찾고, 연결된 User 객체의 ID를 가져오기
        User user = refreshTokenService.findByRefreshToken(refreshToken).getUser();
        // User ID로 User 객체를 조회하지 않고, 바로 User 객체를 사용하기
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}

// 전달받은 리프레시 토큰으로 토큰 유효성 검사를 진행
// 유효한 토큰일 때 리프레스 토큰으로 사용자 id를 찾음
// 토큰 제공자의 generateToken 메서드 호출해 새로운 액세스 토큰 생성