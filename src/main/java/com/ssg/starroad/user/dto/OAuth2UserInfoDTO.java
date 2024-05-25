package com.ssg.starroad.user.dto;

import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.ProviderType;
import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserInfoDTO {
    private String email;
    private String name;
    private String nickname;
    private String imagePath;
    private ProviderType providerType;
    private String providerId;

    // OAuth2User 인터페이스를 구현한 객체에서 필요한 정보를 추출하여 OAuth2UserInfoDTO 객체를 생성
    //  OAuth2 인증 과정에서 제공된 사용자 정보 중 필요한 부분만을 선택하여 DTO에 저장
    public static OAuth2UserInfoDTO fromOAuth2User(OAuth2User oAuth2User, String providerType, String providerId) {
        switch (providerType.toUpperCase()) {
            case "GOOGLE":
                return createGoogleDTO(oAuth2User, providerId);
            case "KAKAO":
                return createKakaoDTO(oAuth2User, providerId);
            case "NAVER":
                return createNaverDTO(oAuth2User, providerId);
            default:
                throw new IllegalArgumentException("Unsupported provider: " + providerType);
        }
    }
    private static OAuth2UserInfoDTO createGoogleDTO(OAuth2User oAuth2User, String providerId) {
        OAuth2UserInfoDTO dto = new OAuth2UserInfoDTO();
        dto.setEmail(oAuth2User.getAttribute("email"));
        dto.setName(oAuth2User.getAttribute("name"));
        dto.setNickname(oAuth2User.getAttribute("name"));
        dto.setImagePath(oAuth2User.getAttribute("picture"));
        dto.setProviderType(ProviderType.GOOGLE);
        dto.setProviderId(providerId);
        return dto;
    }

    private static OAuth2UserInfoDTO createKakaoDTO(OAuth2User oAuth2User, String providerId) {
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        OAuth2UserInfoDTO dto = new OAuth2UserInfoDTO();
        if (kakaoAccount != null) {
            dto.setEmail((String) kakaoAccount.get("email"));
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            dto.setName((String) profile.get("nickname"));
            dto.setImagePath((String) profile.get("thumbnail_image_url"));
        }
        dto.setProviderType(ProviderType.KAKAO);
        dto.setProviderId(providerId);
        return dto;
    }

    private static OAuth2UserInfoDTO createNaverDTO(OAuth2User oAuth2User, String providerId) {
        Map<String, Object> response = oAuth2User.getAttribute("response");
        OAuth2UserInfoDTO dto = new OAuth2UserInfoDTO();
        if (response != null) {
            dto.setEmail((String) response.get("email"));
            dto.setName((String) response.get("name"));
            dto.setImagePath((String) response.get("profile_image"));
        }
        dto.setProviderType(ProviderType.NAVER);
        dto.setProviderId(providerId);
        return dto;
    }

    // User 엔티티의 새 인스턴스를 생성
    // DTO가 서비스 레이어와 데이터 계층 사이에서 엔티티의 생성을 담당
    // 엔티티 인스턴스의 생성 로직과 비즈니스 로직을 분리
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .name(this.name)
                .nickname(this.nickname != null ? this.nickname : generateTempNickname()) // 기본 닉네임 설정
                .imagePath(this.imagePath)
                .providerType(this.providerType)
                .providerId(this.providerId)
                .activeStatus(ActiveStatus.ACTIVE)
                .build();
    }

    private String generateTempNickname() {
        long unixTimestamp = System.currentTimeMillis() / 1000L;
        String timestampStr = String.valueOf(unixTimestamp);
        String lastSixDigits = timestampStr.length() > 6 ? timestampStr.substring(timestampStr.length() - 6) : timestampStr;
        return "star" + lastSixDigits;
    }
}
