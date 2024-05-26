package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.dto.OAuth2UserInfoDTO;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.ProviderType;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.CustomOAuth2UserService;
import com.ssg.starroad.user.service.RefreshTokenService;
import com.ssg.starroad.user.service.oauth.OAuth2UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService implements CustomOAuth2UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = getProviderId(oAuth2User, provider);

        OAuth2UserInfoDTO userInfoDTO = OAuth2UserInfoDTO.fromOAuth2User(oAuth2User, provider, providerId);

        Optional<User> existingUser = userRepository.findByEmail(userInfoDTO.getEmail());
        User user;
        if (existingUser.isPresent()) {
            user = updateExistingUser(existingUser.get(), userInfoDTO);
        } else {
            String nickname = userInfoDTO.getNickname() != null ? userInfoDTO.getNickname() : generateTempNickname();
            user = User.builder()
                    .email(userInfoDTO.getEmail())
                    .name(userInfoDTO.getName())
                    .nickname(nickname)
                    .providerId(userInfoDTO.getProviderId())
                    .providerType(userInfoDTO.getProviderType())
                    .activeStatus(ActiveStatus.ACTIVE)
                    .build();
            user = userRepository.save(user);
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

        // Create a response object that includes the accessToken, refreshToken, and email
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("email", user.getEmail());

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                response,
                "email"
        );
    }

    private String generateTempNickname() {
        long unixTimestamp = System.currentTimeMillis() / 1000L;
        String timestampStr = String.valueOf(unixTimestamp);
        String lastSixDigits = timestampStr.length() > 6 ? timestampStr.substring(timestampStr.length() - 6) : timestampStr;
        return "star" + lastSixDigits;
    }

    private String getProviderId(OAuth2User oAuth2User, String provider) {
        switch (provider.toUpperCase()) {
            case "GOOGLE":
                return oAuth2User.getAttribute("sub");
            case "KAKAO":
                Long kakaoId = oAuth2User.getAttribute("id");
                return kakaoId != null ? String.valueOf(kakaoId) : null;
            case "NAVER":
                Map<String, Object> response = oAuth2User.getAttribute("response");
                if (response != null) {
                    return (String) response.get("id");
                } else {
                    throw new IllegalArgumentException("OAuth2 제공자로부터 providerId를 가져올 수 없습니다.");
                }
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email " + email));
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfoDTO userInfoDTO) {
        return existingUser.toBuilder()
                .name(userInfoDTO.getName())
                .nickname(userInfoDTO.getNickname() != null ? userInfoDTO.getNickname() : existingUser.getNickname())
                .imagePath(userInfoDTO.getImagePath())
                .providerType(userInfoDTO.getProviderType())
                .providerId(userInfoDTO.getProviderId())
                .build();
    }


    @Override
    public OAuth2User loadUserByProvider(String provider, String code) {
        OAuth2AccessToken accessToken = getAccessToken(provider, code);
        OAuth2UserRequest userRequest = createOAuth2UserRequest(provider, accessToken);
        return loadUser(userRequest);
    }

    private OAuth2AccessToken getAccessToken(String provider, String code) {
        ClientRegistration clientRegistration = getClientRegistration(provider);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientRegistration.getClientId());
        params.add("redirect_uri", clientRegistration.getRedirectUri());
        params.add("code", code);
        params.add("client_secret", clientRegistration.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response;

        try {
            response = restTemplate.postForEntity(clientRegistration.getProviderDetails().getTokenUri(), request, Map.class);
            System.out.println("Token request status: " + response.getStatusCode());
            System.out.println("Token request response body: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Failed to retrieve access token: " + e.getMessage());
            throw new OAuth2AuthenticationException("Failed to retrieve access token");
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            System.out.println("Access token response: " + responseBody); // 응답 로그 추가
            String accessToken = (String) responseBody.get("access_token");
            return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, null, null);
        } else {
            System.out.println("Failed to retrieve access token: " + response.getStatusCode());
            throw new OAuth2AuthenticationException("Failed to retrieve access token");
        }
    }

    private OAuth2UserRequest createOAuth2UserRequest(String provider, OAuth2AccessToken accessToken) {
        ClientRegistration clientRegistration = getClientRegistration(provider);
        return new OAuth2UserRequest(clientRegistration, accessToken);
    }

    private ClientRegistration getClientRegistration(String provider) {
        return clientRegistrationRepository.findByRegistrationId(provider);
    }

    @Override
    public Optional<User> processOAuthPostLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            OAuth2UserInfoDTO dto = OAuth2UserInfoDTO.fromOAuth2User(oAuth2User, oAuth2User.getAttribute("provider"), oAuth2User.getName());
            User user = dto.toEntity();
            user = userRepository.save(user);
            return Optional.of(user);
        }

        return userOptional;
    }
}
