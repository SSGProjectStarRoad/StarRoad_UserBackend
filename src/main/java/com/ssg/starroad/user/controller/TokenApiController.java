package com.ssg.starroad.user.controller;

import com.ssg.starroad.user.dto.CreateAccessTokenRequest;
import com.ssg.starroad.user.dto.CreateAccessTokenResponse;
import com.ssg.starroad.user.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 클래스가 RESTful 웹 서비스의 컨트롤러로 작동함
// 모든 메서드는 HTTP 응답 본문에 직접 작성되며, 객체는 자동으로 JSON 또는 XML로 직렬화
@RequestMapping("/api/token")
public class TokenApiController {

    private final TokenService tokenService;

    public TokenApiController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // 클라이언트는 리프레시 토큰을 포함한 CreateAccessTokenRequest 객체를 JSON 형태로 서버에 전송

    // 새 액세스 토큰을 생성하는 요청을 담당
    @PostMapping("/access-token")
    // CreateAccessTokenRequest 객체를 요청 본문에서 받아 처리하고,
    // CreateAccessTokenResponse 객체를 반환
    // @RequestBody 어노테이션은 HTTP 요청 본문을 자바 객체로 변환하는 데 사용
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        // 서버는 요청을 받고 TokenService를 사용하여 리프레시 토큰의 유효성을 검증
        // 유효한 토큰인 경우, 관련된 사용자 정보를 기반으로 새 액세스 토큰을 생성

        return ResponseEntity.ok(new CreateAccessTokenResponse(newAccessToken));
        // 생성된 액세스 토큰을 클라이언트에게 반환하는 데 사용
        // ok() 메서드는 HTTP 200 상태 코드를 설정
        // 응답 본문에는 생성된 토큰이 포함

        // 클라이언트는 이 토큰을 사용하여 권한이 필요한 리소스에 접근 가능
    }
}
