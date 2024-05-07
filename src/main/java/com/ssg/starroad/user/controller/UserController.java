package com.ssg.starroad.user.controller;

import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.dto.LoginRequest;
import com.ssg.starroad.user.dto.LoginResponse;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    public UserController(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        boolean isValidUser = userService.validateUser(request.getEmail(), request.getPassword());
        if (isValidUser) {
            UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
            User user = (User) userDetails; // UserDetails를 User로 캐스팅

            // 토큰 만료 시간 설정: 현재 시간으로부터 10분 후
            Date expiry = new Date(System.currentTimeMillis() + 600 * 1000); // 600 seconds (10 minutes)

            String accessToken = tokenProvider.createToken(expiry, user); // 만료 시간을 인자로 추가
            String refreshToken = refreshTokenService.createRefreshToken(user.getId()); // 리프레시 토큰 생성 로직
            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
