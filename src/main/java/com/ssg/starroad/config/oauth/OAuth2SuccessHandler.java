package com.ssg.starroad.config.oauth;

import com.ssg.starroad.common.util.CookieUtil;
import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    // 액세스 토큰 및 리프레시 토큰 생성과 사용자 정보 조회 기능 활용
    // CustomOAuth2UserService를 사용하여 OAuth2 로그인 성공 후 사용자 정보를 처리

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final int REFRESH_TOKEN_EXPIRY = 60 * 60 * 24 * 14;
    // "60초 * 60분 * 24시간 * 14일" 결과적으로 리프레시 토큰의 유효 기간은 14일(두 주)
    // (액세스 토큰보다 긴 유효 기간)사용자가 액세스 토큰이 만료된 후에도 새로운 액세스 토큰을 얻을 수 있음

    private static final int ACCESS_TOKEN_EXPIRY = 60 * 60 * 24;
    // "60초 * 60분 * 24시간" 결과적으로 액세스 토큰의 유효 기간은 24시간, 즉 1일
    // (리소스에 대한 접근을 제어하는 짧은 유효 기간의 토큰) 사용자가 인증된 상태로 서비스를 이용할 수 있게 함

    public OAuth2SuccessHandler(TokenProvider tokenProvider, CustomOAuth2UserService customOAuth2UserService) {
        this.tokenProvider = tokenProvider;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    public static int getRefreshTokenExpiry() {
        return REFRESH_TOKEN_EXPIRY;
    }

    public static int getAccessTokenExpiry() {
        return ACCESS_TOKEN_EXPIRY;
    }


    // OAuth2 인증 과정이 성공적으로 완료된 후 실행되는 메소드
    // 1) 사용자 인증 정보를 활용하여 토큰을 생성하고,
    // 2) 이를 클라이언트 측에 전달하기 위해 쿠키에 저장
    // 3) 사용자를 최종 목적지 URL로 리디렉션
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauthToken.getPrincipal();

            // 이메일을 통해 사용자 정보 조회
            User user = customOAuth2UserService.findUserByEmail(oAuth2User.getAttribute("email"));

            // 토큰 생성
            String refreshToken = tokenProvider.generateToken(user, Duration.ofSeconds(REFRESH_TOKEN_EXPIRY));
            String accessToken = tokenProvider.generateToken(user, Duration.ofSeconds(ACCESS_TOKEN_EXPIRY));

            // 쿠키에 토큰 추가
            addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, REFRESH_TOKEN_EXPIRY);
            addCookie(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_TOKEN_EXPIRY);

            // 세션 무효화
            request.getSession().invalidate();

            // 최종 목적지 URL로 리다이렉트
            String targetUrl = determineTargetUrl(request, response, authentication);
            clearAuthenticationAttributes(request);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            throw new RuntimeException("OAuth2 인증 성공 처리 중 오류 발생", e);
        }
    }

    // 지정된 이름, 값, 최대 유지 시간을 가진 쿠키를 HTTP 응답에 추가하는 메소드
    // 인증 토큰을 클라이언트에게 안전하게 전달하는 데 사용
    // addCookie 메소드 하나만 있어도 충분히 토큰 종류(액세스/리프레시)를 구별하여 쿠키에 저장
    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        CookieUtil.addCookie(response,name, value, maxAge);
    }

    // 인증 성공 후 리디렉션할 최종 URL을 결정하는 메소드
    // URL은 사용자가 인증 후 이동할 최종 목적지를 지정
    // UriComponentsBuilder를 사용하여 리디렉션할 URL을 구성하고, 이를 문자열로 반환
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return UriComponentsBuilder.fromUriString("/main")
                .build().toUriString();
    }
}
