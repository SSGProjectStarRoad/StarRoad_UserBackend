package com.ssg.starroad.user.controller;

import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.config.oauth.OAuth2SuccessHandler;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final TokenProvider tokenProvider;


    @PostMapping("/{provider}")
    public ResponseEntity<?> oauth2Login(@PathVariable String provider, @RequestBody Map<String, String> body) {
        String code = body.get("code");
        OAuth2User oAuth2User = customOAuth2UserService.loadUserByProvider(provider, code);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String accessToken = (String) attributes.get("accessToken");
        String refreshToken = (String) attributes.get("refreshToken");
        String email = (String) attributes.get("email");

        // Create a response object
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("email", email);

        return ResponseEntity.ok(response);
    }
}