package com.ssg.starroad.user.service;

import com.ssg.starroad.user.entity.User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Optional;

public interface CustomOAuth2UserService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    OAuth2User loadUser(OAuth2UserRequest userRequest);

    User findUserByEmail(String email);

    Optional<User> processOAuthPostLogin(OAuth2User oAuth2User);

    OAuth2User loadUserByProvider(String provider, String code);
}

// OAuth2 프로세스는 사용자가 예를 들어 Google이나 Facebook 같은 외부 서비스를 사용하여 로그인할 때 발생함
// 사용자가 이러한 서비스를 통해 로그인을 시도하면,
// 이 서비스는 사용자의 정보(예: 이름, 이메일, 프로필 사진 등)를 포함하는 OAuth2UserRequest를 생성하여 애플리케이션에 보냄
// OAuth2UserRequest는 OAuth2UserService에 의해 처리되며,
// 이 서비스는 외부 시스템에서 제공하는 정보를 기반으로 OAuth2User 객체를 생성
// 이 객체는 Spring Security 컨텍스트에서 사용자의 인증 정보로 활용

// OAuth2UserService를 상속받아 구현되어야 하는 이유
// 외부 서비스로부터 받은 사용자 정보(OAuth2UserRequest)를 애플리케이션의 요구사항에 맞게 처리하고,
// 이를 OAuth2User 객체로 변환하여 Spring Security에 통합하기 위해