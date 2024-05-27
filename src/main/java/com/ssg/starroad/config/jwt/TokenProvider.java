package com.ssg.starroad.config.jwt;
// JWT를 생성하고 파싱하여 유효성을 검사하는 역할
// JwtProperties 클래스에서 설정된 값을 사용하여 토큰을 관리

import com.ssg.starroad.user.entity.User;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return createToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }
    // 사용자 객체와 만료 시간(Duration)을 받아 jwt 토큰 생성
    // 파라미터 (사용자 정보, 토큰 유효 기간)
    // 현재 시간을 구하고 expiredAt으로 전달된 기간을 현재 시간에 더해 토큰 만료 시간 expiry 계산
    // creatToken 메소드 호출 실제 토큰 생성


    // 실제 jwt 토큰 생성 사용자 정보와 만료 시간을 받아 구성, 서명해 반환
    public String createToken(Date expiry, User user) {

        Date now = new Date();
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));


        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // 헤더 타입 jwt
                // 내용 jwt의 클레임 설정
                .setIssuer(jwtProperties.getIssuer()) // 토큰 발급자
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expiry) // 토큰 만료 시간
                .setSubject(user.getEmail()) // 사용자 이메일
                .claim("userId", user.getId()) // 추가 정보 클레임 id : 유저 아이디
                // 토큰에 사용자 고유 id 정보 담아 토큰 해석 시 사용자 정보 접근에 용이
                // 서명 비밀값과 합께 해시값을 HS256 방식으로 암호화
                .signWith(key)
                .compact();
    }

    // 제공된 토큰이 유효한지 검사. 토큰의 서명 검증. 파싱 중 발생할 수 있는 예외 처리
// 예외가 발생하면 false 그렇지 않으면 true
    public boolean validToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            Date expiration = claims.getExpiration();
            System.out.println("Token expiration time: " + expiration);

            return !expiration.before(new Date());
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }


    // 토큰 기반으로 Authentication 인증 객체 생성. 시큐리티에서 사용자의 인증 정보 나타내는 데 사용
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        // 토큰에서 클레임 추출
        // 클레임에서 사용자 역할 포함하는 authorities 설정
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User
                (claims.getSubject(), "", authorities), token, authorities);
    }

    // 토큰에서 사용자 id 추출
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Long.class);
    }

    // 토큰에서 클레임을 추출하는 보조 메소드
    // 토큰의 파싱 담당 클레임 세트 반환
    private Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
        return Jwts.parserBuilder() // 클레임 조회
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(User user) {
        // 액세스 토큰의 유효 기간을 설정합니다 (예: 1시간)
        Duration accessTokenValidity = Duration.ofHours(1);
        return generateToken(user, accessTokenValidity);
    }
}
