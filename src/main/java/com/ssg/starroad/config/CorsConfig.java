package com.ssg.starroad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://223.130.155.196:8080"); // 프로덕션용
        config.addAllowedOriginPattern("http://localhost:8080"); // Vue 애플리케이션 주소 추가

        config.addAllowedHeader("*"); // 서버가 받을 수 있는 모든 헤더를 정의

        // 각 HTTP 메소드를 개별적으로 추가
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        config.addExposedHeader("Authorization"); // 클라이언트가 읽을 수 있도록 서버의 응답에서 노출할 헤더를 정의
        config.addExposedHeader("refreshToken");
        source.registerCorsConfiguration("/**", config); // 모든 경로에 CORS 설정 적용
        return source;
    }
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
