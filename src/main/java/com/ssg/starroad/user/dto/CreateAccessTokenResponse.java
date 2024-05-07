package com.ssg.starroad.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 모든 필드를 매개변수로 받는 생성자 생성
// 모든 필드 값을 초기화해야 할 필요가 있음
// 생성 시점에 모든 데이터를 받아 객체 상태를 완전히 정의해야 하는 경우 사용
// 생성된 액세스 토큰을 필수적으로 포함해 인스턴스를 생성해야 해서 모든 인자를 받는 생성자가 필요함!

// 새로 생성된 액세스 토큰을 클라이언트에게 반환할 때 사용
// 클라이언트에게 액세스 토큰 정보를 제공하기 위한 목적!
public class CreateAccessTokenResponse {
    private String accessToken; // 생성된 액세스 토큰
}