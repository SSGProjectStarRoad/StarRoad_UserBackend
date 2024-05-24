package com.ssg.starroad.user.service;

public interface TokenService {
    String createNewAccessToken(String refreshToken);
}
