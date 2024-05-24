package com.ssg.starroad.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateUniqueToken() {
        byte[] randomBytes = new byte[24]; // 192 bits
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}

// SecureRandom을 사용하여 토큰을 생성하는 것은 토큰 값이 완전히 무작위이고, 외부에서 예측할 수 없게 만들기
// SecureRandom은 암호학적으로 안전한 무작위 수 생성기로, 높은 수준의 무작위성을 보장

// TokenGenerator에서 고유한 리프레시 토큰을 생성하는 로직을 구성하는 것은
// 보안, 데이터 무결성, 그리고 사용자 간의 간섭 방지를 위해 필수적