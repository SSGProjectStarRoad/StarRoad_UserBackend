package com.ssg.starroad.config.jwt;


import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.Gender;
import com.ssg.starroad.user.enums.ProviderType;
import com.ssg.starroad.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @BeforeEach
    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // given : 테스트를 위한 사용자 정보를 설정하고 데이터베이스에 저장
        User testUser = userRepository.save(User.builder()
                .name("홍길동")
                .nickname("gildong123")
                .password("password@123") // 비밀번호 패턴을 준수해야 합니다.
                .email("gildong@example.com")
                .gender(Gender.MALE)
                .birth(LocalDate.of(1990, 1, 1))
                .phone("01012345678")
                .providerType(ProviderType.GOOGLE)
                .providerId("1234567890")
                .activeStatus(ActiveStatus.ACTIVE)
                .build());
        // when : 토큰 생성 메서드를 호출하여 JWT 토큰을 생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then : 생성된 토큰에서 사용자 ID를 추출하여 저장된 사용자의 ID와 비교
        // 토큰 복호화. 토큰 만들 때 클레임으로 넣어둔 id 값이 given 절에서 만든 사용자 id랑 동일한지 확인
        Long userId = tokenProvider.getUserId(token);
        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        // given : 과거 날짜로 설정된 토큰의 만료 시간을 설정
        Date pastDate = new Date(new Date().getTime() - 86400000); // 하루 지났음
        JwtFactory jwtFactory = JwtFactory.builder()
                .expiration(pastDate)
                .build();
        String token = jwtFactory.createToken(jwtProperties);

        // when : 토큰의 유효성 검증을 수행
        boolean result = tokenProvider.validToken(token);

        // then : 토큰이 유효하지 않으므로 결과는 false
        assertThat(result).isFalse();
    }


    @DisplayName("validToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken() {
        // given : 유효성 검증에 사용할 기본 설정의 토큰 생성
        JwtFactory jwtFactory = JwtFactory.withDefaultValues();
        String token = jwtFactory.createToken(jwtProperties);

        // when : 토큰의 유효성 검증을 수행
        boolean result = tokenProvider.validToken(token);

        // then : 토큰이 유효하므로 결과는 true
        assertThat(result).isTrue();
    }


    @DisplayName("getAuthentication(): 토큰 기반으로 인증정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // given : 인증 정보 생성을 위해 사용자를 저장하고 토큰을 생성
        User testUser = userRepository.save(User.builder()
                .email("gildong@example.com")
                .password("password@123")
                .build());

        // when : 생성된 토큰을 사용하여 인증 정보를 얻음
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then : 인증 정보에서 얻은 사용자 이름이 저장된 사용자의 이메일과 일치하는지 확인
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(testUser.getEmail());
    }

    @Rollback(false)  // 특정 테스트에서 롤백 비활성화 예시
    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        // given : 테스트를 위해 사용자를 저장하고 토큰을 생성
        User testUser = userRepository.save(User.builder().email("gildong@example.com").build());
        String token = tokenProvider.generateToken(testUser, java.time.Duration.ofDays(1));
        // Duration expiredAt : java.time.Duration.ofDays(1)을 사용하여 토큰의 유효 기간을 1일로 설정
        // when : 생성된 토큰으로부터 사용자 ID를 추출
        Long userIdByToken = tokenProvider.getUserId(token);

        // then : 추출된 ID가 저장된 사용자의 ID와 일치하는지 확인
        assertThat(userIdByToken).isEqualTo(testUser.getId());
    }
}