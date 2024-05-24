package com.ssg.starroad.user.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private String email;
    private String accessToken;
    private String refreshToken;

}
