package com.ssg.starroad.user.controller;

import com.ssg.starroad.user.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-code")
    public ResponseEntity<String> sendAuthCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        try {
            authService.sendAuthCode(email);
            return ResponseEntity.ok("인증 코드가 전송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("이메일 전송에 실패했습니다.");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyAuthCode(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        int code = Integer.parseInt(payload.get("code").toString());
        boolean isVerified = authService.verifyAuthCode(email, code);
        if (isVerified) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(400).body(false);
        }
    }
}
