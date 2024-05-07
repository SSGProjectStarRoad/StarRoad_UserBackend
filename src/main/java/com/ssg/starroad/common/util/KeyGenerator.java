package com.ssg.starroad.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 32바이트로 256비트 키 생성
        random.nextBytes(bytes);
        String encodedKey = Base64.getEncoder().encodeToString(bytes);
        System.out.println("생성된 Base64 인코딩된 비밀 키: " + encodedKey);
    }
}
