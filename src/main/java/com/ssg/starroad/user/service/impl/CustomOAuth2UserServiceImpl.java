package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ProviderType;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.CustomOAuth2UserService;
import com.ssg.starroad.user.service.oauth.OAuth2UserData;
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
    private final OAuth2UserData oauth2UserData;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName();

        User user = oauth2UserData.oauth2Info(oAuth2User, provider, providerId);

        if (user.getEmail() == null) {
            throw new IllegalArgumentException("OAuth2 제공자로부터 이메일을 가져올 수 없습니다.");
        }

        User savedUser = userRepository.findByEmail(user.getEmail())
                .map(existingUser -> updateExistingUser(existingUser, oAuth2User))
                .orElseGet(() -> user);

        userRepository.save(savedUser);
        return createOAuth2User(savedUser);
    }
    private OAuth2User createOAuth2User(User user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", user.getEmail());
        attributes.put("name", user.getName());
        attributes.put("nickname", user.getNickname());
        attributes.put("phone", user.getPhone());
        attributes.put("imagePath", user.getImagePath());

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new DefaultOAuth2User(authorities, attributes, "email");
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email " + email));
    }

    // OAuth2 로그인 후 사용자 처리
    @Override
    public void processOAuthPostLogin(OAuth2User oAuth2User) {
        String provider = oAuth2User.getAttribute("provider");
        String providerId = oAuth2User.getName();
        User user = oauth2UserData.oauth2Info(oAuth2User, provider, providerId);
        userRepository.save(user); // 사용자 정보 저장
    }

    private User updateExistingUser(User existingUser, OAuth2User oAuth2User) {
        User updatedUser = existingUser.toBuilder()
                .name(oAuth2User.getAttribute("name"))
                .nickname(oAuth2User.getAttribute("nickname"))
                .phone(oAuth2User.getAttribute("phone"))
                .imagePath(oAuth2User.getAttribute("picture"))
                .providerType(ProviderType.fromString(oAuth2User.getAttribute("provider")))
                .providerId(oAuth2User.getName())
                .build();
        userRepository.save(updatedUser); // 변경된 사용자 정보 저장
        return updatedUser;
    }
}

