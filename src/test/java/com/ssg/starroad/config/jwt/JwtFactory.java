package com.ssg.starroad.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

// 테스트 실행용 Mocking(모킹) 객체 => 가짜 객체
@Getter
public class JwtFactory {

    private String subject = "gildong@example.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = emptyMap();
    // 비어 있는 맵 반환 클레임 포함하지 않는 기본 설정으로 토큰 생성할 때 사용됨
    // 테스트 시나리오에서 클레임 추가하기 전 기본 상태 제공

    @Builder
    // 빌더 패턴을 사용해 인스턴스 초기화
    // 파라미터가 null이 아닌 경우에만 해당 파라미터로 필드를 초기화하고
    // 그렇지 않으면 기본값을 사용
    public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    // 기본 생성자를 통해 객체 생성
    // 추가 설정 없이 기본값으로 토큰 생성
    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    // jwt 토큰 생성 메소드
    // jwt 표준 필드와 함께 사용자 정의 클레임을 토큰에 추가
    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)  // 수정된 부분: 다양한 사용자 정의 클레임을 포함할 수 있도록 수정
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
}

