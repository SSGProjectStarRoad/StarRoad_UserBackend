package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.ProviderType;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService implements CustomOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return saveOrUpdate(userRequest, oAuth2User);
    }
    // OAuth2 프로바이더에서 사용자 정보를 로드
    // super.loadUser(userRequest)를 호출하여 기본 OAuth2 사용자 정보를 가져오기

    // 데이터베이스에서 사용자 이메일을 기준으로 기존 사용자를 검색
    // updateExistingUser를 호출하여 정보를 업데이트하고,
    // 존재하지 않는다면 registerNewUser를 호출하여 새로운 사용자를 등록
    // 사용자 정보를 DefaultOAuth2User로 변환하여 반환
    private OAuth2User saveOrUpdate(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser, oAuth2User))
                .orElseGet(() -> registerNewUser(userRequest, oAuth2User));

        Map<String, Object> attributes = buildAttributes(user);
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new DefaultOAuth2User(authorities, attributes, "email");
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String providerId = userRequest.getClientRegistration().getRegistrationId();
        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            throw new IllegalArgumentException("이메일은 필수 정보입니다.");
        }

        ProviderType providerType = ProviderType.fromString(providerId);
        // providerId를 ProviderType 열거형으로 변환

        User newUser = User.builder()
                .email(email)
                .name(oAuth2User.getAttribute("name"))
                .nickname(oAuth2User.getAttribute("nickname"))
                .phone(oAuth2User.getAttribute("phone"))
                .imagePath(oAuth2User.getAttribute("picture"))
                .providerType(providerType)
                .birth(null)  // 필요한 경우 적절한 로직으로 채우기
                .providerId(providerId)
                .reviewExp(0)
                .point(0)
                .activeStatus(ActiveStatus.ACTIVE)
                .build();

        return userRepository.save(newUser);
    }

    private User updateExistingUser(User existingUser, OAuth2User oAuth2User) {
        User updatedUser = existingUser.toBuilder()
                .name(oAuth2User.getAttribute("name"))
                .nickname(oAuth2User.getAttribute("nickname"))
                .phone(oAuth2User.getAttribute("phone"))
                .build();

        return userRepository.save(updatedUser);
    }

    private Map<String, Object> buildAttributes(User user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", user.getName());
        attributes.put("email", user.getEmail());
        attributes.put("nickname", user.getNickname());
        attributes.put("gender", user.getGender().name());
        attributes.put("birth", user.getBirth());
        attributes.put("phone", user.getPhone());
        attributes.put("imagePath", user.getImagePath());
        attributes.put("providerType", user.getProviderType());
        attributes.put("providerId", user.getProviderId());
        attributes.put("reviewExp", user.getReviewExp());
        attributes.put("point", user.getPoint());
        attributes.put("activeStatus", user.getActiveStatus().name());
        return attributes;
    }
}

