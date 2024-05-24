package com.ssg.starroad.user.service;

import jakarta.mail.MessagingException;

public interface AuthService {
    void sendAuthCode(String email) throws MessagingException;
    boolean verifyAuthCode(String email, int authCode);
}
