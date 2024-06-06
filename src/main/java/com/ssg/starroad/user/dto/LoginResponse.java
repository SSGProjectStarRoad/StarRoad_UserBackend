package com.ssg.starroad.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String message;


    public LoginResponse(String message) {
        this.message = message;
    }

}
// 응답에서 리프레시 토큰을 포함하는 이유 ---
// 사용자가 로그인할 때 마다 새로운 리프레시 토큰을 발급하는 것은,
// 토큰의 보안을 강화하고, 오래된 또는 잠재적으로 노출된 리프레시 토큰을 무효화하는 효과적인 방법
// 일부 시스템에서는 리프레시 토큰의 재사용을 방지하기 위해 매 로그인 시 또는 매 토큰 갱신 시
// 새로운 리프레시 토큰을 발급하고 이전 토큰을 무효화
// 따라서, 로그인 응답에 리프레시 토큰을 포함하는 것은
// 토큰 기반 인증 시스템에서 흔히 볼 수 있는 보안 관행이며, 이를 통해 사용자의 로그인 상태를 안전하게 유지 가능