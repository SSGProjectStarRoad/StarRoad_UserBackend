package com.ssg.starroad.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 추가
@NoArgsConstructor
// 매개변수 없는 기본 생성자 생성
// 데이터 바인딩 사용할 때 객체를 생성한 후 세터 메소드를 통해
// 값을 주입할 수 있기 때문에 기본 생성자가 필요
// 예를 들어, json 형태의 http 요청 바디를 객체로 변환할 때 사용

// 클라이언트로부터 리프레시 토큰을 받아 새로운 액세스 토큰을 생성하기 위한
// 요청 정보를 담는 역할! 여기엔 보통 리프레스 토큰이 포함되어 있음
// 사용자로부터 리프레시 토큰을 입력받는 용도!
public class CreateAccessTokenRequest {
    private String refreshToken; // 클라이언트가 제공하는 리프레시 토큰
}

