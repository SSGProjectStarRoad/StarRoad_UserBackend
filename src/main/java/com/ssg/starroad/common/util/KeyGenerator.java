package com.ssg.starroad.common.util;

import java.security.SecureRandom;
import java.util.Base64;

// 주로 암호화 또는 토큰 서명에 사용
// JWT 토큰을 서명할 때 사용되는 비밀키는 해당 토큰의 보안을 유지하는 데 필수적
// 토큰의 내용을 암호화하거나 서명하는 데 사용되어, 토큰이 조작되지 않았음을 검증 가능
public class KeyGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 32바이트로 256비트 키 생성
        random.nextBytes(bytes);
        String encodedKey = Base64.getEncoder().encodeToString(bytes);
        System.out.println("생성된 Base64 인코딩된 비밀 키: " + encodedKey);
    }
}
