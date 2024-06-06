package com.ssg.starroad.user.service;

import com.ssg.starroad.user.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken findByRefreshToken(String refreshToken);

    String createRefreshToken(Long userId);
}
