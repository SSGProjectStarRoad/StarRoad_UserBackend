package com.ssg.starroad.config;

import com.ssg.starroad.user.service.UserService;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화!!!
                // API는 일반적으로 토큰 기반 인증, 예를 들어 JWT (JSON Web Tokens) 같은 시스템을 사용
                // 이러한 토큰 기반 인증은 사용자의 세션 상태를 서버에 저장하지 않기 때문에 "무상태(stateless)"로 간주
                // 사용자가 직접 요청에 토큰을 첨부해야 하므로, 악의적인 사이트가 사용자를 속여 토큰을 사용하는 요청을 생성하기 어려움
                // 이는 CSRF 공격을 자동으로 방지하는 효과를 가짐
                .authorizeRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/join").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/main", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout") // 로그아웃 경로 설정
                        .logoutSuccessUrl("/user/login")
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        // JSESSIONID : 세션 식별자(서버가 사용자의 세션을 관리하는 데 사용)
                        // 로그아웃 시 사용자의 브라우저에서 서버에 대해 인증 없이 재접속하는 것을 방지하기 위함
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}