package com.ssg.starroad.user.service.oauth;

import com.ssg.starroad.user.dto.OAuth2UserInfoDTO;
import com.ssg.starroad.user.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserInfo implements OAuth2UserData {
    @Override
    public User oauth2Info(OAuth2User oAuth2User, String providerType, String providerId) {
        OAuth2UserInfoDTO dto = OAuth2UserInfoDTO.fromOAuth2User(oAuth2User, providerType, providerId);
        return dto.toEntity();
    }
}