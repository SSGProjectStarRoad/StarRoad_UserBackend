package com.ssg.starroad.config;

import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Spring Security의 일부로서 인증 과정에서 중요한 역할
// HTTP 요청에서 인증 토큰 (예: JWT)을 추출하고, 이를 검증하여 사용자의 인증 상태를 결정하는 것
// Spring Security에서 제공하는 OncePerRequestFilter 클래스를 상속받아 구현하는 것이 일반적
// OncePerRequestFilter를 사용하면, 필터가 한 요청에 대해 한 번만 실행되는 것을 보장
// doFilterInternal 메소드를 오버라이드하여 필터의 로직을 구현
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    // 토큰 생성하고 검증하는 컴포넌트

    private final static String HEADER_AUTHORIZATION = "Authorization";
    // HTTP 요청에서 사용되는 헤더 필드의 이름 정의
    // 인증 토큰은 보통 이 헤더에 "Bearer" 형식으로 포함됨
    // 소유자라는 뜻인데 이 토큰의 소유자에게 권한을 부여해

    private final static String TOKEN_PREFIX = "Bearer ";
    // 인증 헤더에 사용되는 토큰의 접두사 실제 토큰 값만 추출하기 위해 사용

    private final static Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    // 로깅

    // 실제 필터의 로직을 구현하는 메소드
    // 요청의 헤더에서 토큰을 추출하고, 이 토큰이 유효한 경우에만
    // Spring Security의 SecurityContext에 인증 객체(Authentication)를 설정
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. HTTP 요청에서 Authorization 헤더는 사용자 인증 정보를 전송하는 데 사용되는 표준 헤더
        // 특히 보안이 필요한 애플리케이션에서 이 헤더를 사용하여 클라이언트가 서버에 자신의 인증 정보를 제공
        // Bearer Token (주로 JWT 사용): 클라이언트는 접근 토큰을 'Bearer' 라는 접두사와 함께 전송
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        // 2. TokenProvider를 사용하여 토큰 검증
        if (token != null) {
            try {
                // 3. 유효한 토큰인 경우, 사용자 인증 객체를 생성하고 SecurityContextHolder에 등록
                if (tokenProvider.validToken(token)) {
                    Authentication authentication = tokenProvider.getAuthentication(token);

                    // 사용자 상태 확인
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    if (userDetails instanceof User) {
                        User user = (User) userDetails;
                        if (!user.isEnabled()) {
                            log.info("User is not enabled: {}", user.getUsername());
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return;
                        }
                    }

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.info("JWT가 유효하지 않음: {}", token);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                // 4. 유효하지 않은 토큰 또는 예외 발생 시 적절한 HTTP 상태 코드 설정
            } catch (Exception e) {
                log.error("Authentication error: ", e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 실제 토큰 값을 추출
    // TOKEN_PREFIX는 "Bearer "로 설정되어 있으며, 이는 토큰 타입을 명시하는 표준 방식
    // substring 메소드를 사용하여 "Bearer " 다음의 문자열을 잘라내고, 실제 토큰 값을 반환
    // 이렇게 추출된 토큰은 유효성 검사를 위해 TokenProvider에 전달
    // 유효한 경우 사용자 인증 정보를 생성하여 SecurityContext에 설정
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    // 특정 경로에 대해 필터를 적용하지 않도록 설정
    // 일반적으로 인증이 필요 없는 공개 API 경로에서 필터를 적용하지 않도록 하여 성능을 최적화
    // 로그인, 회원 가입, 공개적으로 접근 가능한 정보 제공 API 등 인증이 필요 없는 경로들에 대해 필터를 적용하지 않도록 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return  path.startsWith("/user/login") ||
                path.startsWith("/user/join");
    }
}