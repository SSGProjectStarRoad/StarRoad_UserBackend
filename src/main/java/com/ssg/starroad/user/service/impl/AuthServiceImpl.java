package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.entity.AuthCode;
import com.ssg.starroad.user.repository.AuthCodeRepository;
import com.ssg.starroad.user.service.AuthService;
import com.ssg.starroad.user.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthCodeRepository authCodeRepository;
    private final EmailService emailService;
    private final Random random = new Random();

    @Autowired
    public AuthServiceImpl(AuthCodeRepository authCodeRepository, EmailService emailService) {
        this.authCodeRepository = authCodeRepository;
        this.emailService = emailService;
    }

    @Override
    public void sendAuthCode(String email) throws MessagingException {
        int authCode = random.nextInt(9000) + 1000; // 4자리 랜덤 숫자 생성
        AuthCode code = new AuthCode(email, authCode);
        authCodeRepository.save(code);

        emailService.sendEmail(email, "인증 코드", "인증 코드는 " + authCode + " 입니다.");
    }

    @Override
    public boolean verifyAuthCode(String email, int authCode) {
        Optional<AuthCode> storedCode = authCodeRepository.findById(email);
        if (storedCode.isPresent() && storedCode.get().getCode() == authCode) {
            authCodeRepository.delete(storedCode.get());
            return true;
        }
        return false;
    }
}
