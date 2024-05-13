package com.ssg.starroad.user.controller;

import com.ssg.starroad.user.service.CustomOAuth2UserService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final CustomOAuth2UserService customOAuth2UserService;

    public OAuth2Controller(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @GetMapping("/callback")
    public String handleOAuth2Callback(OAuth2AuthenticationToken authentication) {
        // OAuth2AuthenticationToken에서 직접 OAuth2User를 받아오기
        OAuth2User oAuth2User = authentication.getPrincipal();
        customOAuth2UserService.processOAuthPostLogin(oAuth2User);
        return "redirect:/main";
    }
}
