package com.ssg.starroad.user.enums;

public enum ProviderType {
    NAVER, KAKAO, GOOGLE, LOCAL;

    // 입력받은 문자열 providerId를 대문자로 변환
    // ProviderType의 valueOf 메소드를 사용하여 해당 문자열과 일치하는 열거형 상수를 반환
    public static ProviderType fromString(String providerId) {
        if (providerId == null) {
            throw new IllegalArgumentException("providerId is null");
        }
        try {
            return ProviderType.valueOf(providerId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("providerId에 맞는 열거형 값이 없음: " + providerId, e);
        }
    }
}
