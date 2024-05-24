package com.ssg.starroad.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity

// 1. 장기 인증 지원: 리프레시 토큰은 일반적으로 액세스 토큰보다 훨씬 긴 수명을 가짐.
// 그래서 사용자는 자주 로그인할 필요 없이 지속적으로 서비스를 이용 가능.
// 2. 보안성 향상: 액세스 토큰은 짧은 수명을 가지므로, 만약 이 토큰이 탈취당하더라도 사용할 수 있는 시간이 제한적.
// 리프레시 토큰을 이용해 새 액세스 토큰을 발급받는 방식은 액세스 토큰이 노출될 위험을 줄여줍니다.
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // RefreshToken 인스턴스를 유일하게 식별하는 데 사용
    // 자동 생성된 ID는 토큰 관리를 더욱 효율적으로

    @Column(nullable = false, unique = true)
    private String token;
    // 리프레시 토큰은 보안 상 중요한 정보
    // 각 토큰 값은 유일무이, 사용자 인증이나 세션 관리 시 안전하게 사용 가능

    @Setter
    @Column(nullable = false)
    private Instant expiryDate;
    // 만료 시간은 토큰의 유효성을 검증할 때 중요한 역할
    // 만료된 토큰은 자동으로 무효화
    // 세계 표준시(UTC)를 기준
    // 시간의 덧셈, 뺄셈과 같은 시간 연산을 수행하는 다양한 메소드를 제공
    // 예 : Instant.now().plusSeconds(3600)은 현재 시간으로부터 1시간 후

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    // RefreshToken과 User 엔티티 간의 일대일 관계를 설정
    // 토큰이 특정 사용자에게 속함을 명확히
    // 지연 로딩(FetchType.LAZY)을 사용해 성능을 최적화
    // User 엔티티는 실제로 접근할 때만 데이터베이스에서 로드


    public RefreshToken(User user, String token, Instant expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
    public void updateToken(String newToken) {
        this.token = newToken;
    }

    // 새로운 리프레시 토큰이 발행될 때 사용
}
