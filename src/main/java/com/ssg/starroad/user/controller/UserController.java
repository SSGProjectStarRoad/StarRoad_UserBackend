package com.ssg.starroad.user.controller;

import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.dto.*;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.RefreshTokenRepository;
import com.ssg.starroad.user.service.RefreshTokenService;
import com.ssg.starroad.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        boolean isValidUser = userService.validateUser(request.getEmail(), request.getPassword());
        if (isValidUser) {
            UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user == null || !user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("비활성화 계정입니다. 이메일로 문의해주세요."));
            }

            Date expiry = new Date(System.currentTimeMillis() + 600 * 1000); // 10 minutes
            String accessToken = tokenProvider.createToken(expiry, user);
            String refreshToken = refreshTokenService.createRefreshToken(user.getId());

            // 설정된 헤더를 응답에 추가
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken); // Bearer 타입의 Authorization 헤더 추가

            LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken,"환영합니다!");
            return ResponseEntity.ok().headers(headers).body(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid credentials"));
    }

    @PostMapping("/join/email-check")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestBody Map<String, String> request) {
        boolean exists = userService.isEmailDuplicate(request.get("email"));
        System.out.println(exists);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/join/nickname-check")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestBody Map<String, String> request) {
        boolean exists = userService.isNicknameDuplicate(request.get("nickname"));
        System.out.println(exists);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/join")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest request) {
        System.out.println("Received request to update password for email: " + request.getEmail()); // 로그 추가
        try {
            boolean isUpdated = userService.updatePassword(request.getEmail(), request.getNewPassword());
            if (isUpdated) {
                return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }

    // 사용자 정보를 가져오는 메소드
    @PostMapping("/details")
    public ResponseEntity<UserDTO> getUserDetails(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            UserDTO userDTO = new UserDTO(userOptional.get());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            Optional<User> userOptional = userService.findByEmail(userDTO.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                userService.updateUserProfile(user, userDTO);
                return ResponseEntity.ok("프로필이 성공적으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 업데이트 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest, HttpServletRequest request) {
        String email = logoutRequest.getEmail();
        String accessToken = logoutRequest.getAccessToken();

        if (tokenProvider.validToken(accessToken)) {
            // 이메일과 토큰으로 사용자 인증 및 토큰 무효화 로직 추가
            refreshTokenRepository.deleteByUserEmail(email);
        }
        // 세션 무효화
        request.getSession().invalidate();

        return ResponseEntity.ok().body("Logout successful");
    }

    @PostMapping("/inactive")
    public ResponseEntity<String> deactivateUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        userService.inactiveUser(email);
        return ResponseEntity.ok("User deactivated successfully");

    }
}