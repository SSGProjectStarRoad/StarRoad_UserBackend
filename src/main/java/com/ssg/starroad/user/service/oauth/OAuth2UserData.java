package com.ssg.starroad.user.service.oauth;

import com.ssg.starroad.user.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;


public interface OAuth2UserData {
    User oauth2Info(OAuth2User oAuth2User, String providerType, String providerId);
}
