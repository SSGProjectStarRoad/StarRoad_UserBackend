package com.ssg.starroad.user.controller;

import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.dto.LoginRequest;
import com.ssg.starroad.user.dto.LoginResponse;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.service.RefreshTokenService;
import com.ssg.starroad.user.service.UserService;
import org.springframework.http.HttpHeaders;
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
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, TokenProvider tokenProvider, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        boolean isValidUser = userService.validateUser(request.getEmail(), request.getPassword());
        if (isValidUser) {
            UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Date expiry = new Date(System.currentTimeMillis() + 600 * 1000); // 10 minutes
            String accessToken = tokenProvider.createToken(expiry, user);
            String refreshToken = refreshTokenService.createRefreshToken(user.getId());

            // 설정된 헤더를 응답에 추가
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken); // Bearer 타입의 Authorization 헤더 추가

            LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);
            return ResponseEntity.ok().headers(headers).body(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}