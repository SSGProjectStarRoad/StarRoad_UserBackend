package com.ssg.starroad.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.config.jwt.JwtFactory;
import com.ssg.starroad.config.jwt.JwtProperties;
import com.ssg.starroad.user.dto.CreateAccessTokenRequest;
import com.ssg.starroad.user.entity.RefreshToken;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.Gender;
import com.ssg.starroad.user.enums.ProviderType;
import com.ssg.starroad.user.repository.RefreshTokenRepository;
import com.ssg.starroad.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    // 웹 서버를 실행하지 않고도 스프링 MVC(웹 계층)의 동작을 시뮬레이션 할 수 있게 해주는 클래스

    @Autowired
    private ObjectMapper objectMapper;
    // JSON 데이터와 Java 객체 간의 변환을 처리

    @Autowired
    private WebApplicationContext context;
    // 스프링 애플리케이션 컨텍스트를 제공

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    // 테스트 실행 전에 필요한 초기 설정을 수행
    // MockMvc 인스턴스를 구성하고 데이터베이스를 초기화
    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @DisplayName("createAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createAccessToken() throws Exception {
        // given
        final String url = "/api/token/access-token";

        // 테스트용 사용자를 생성하고 데이터베이스에 저장
        User testUser = userRepository.save(User.builder()
                .email("user@example.com")
                .password("Valid123$") // 비밀번호는 유효성 규칙에 맞춰 생성
                .name("John Doe")
                .nickname("JD2024") // 닉네임은 유니크해야 함, 데이터베이스 상황에 따라 수정 필요
                .gender(Gender.MALE) // 성별은 ENUM 값 중 하나
                .birth(LocalDate.of(1990, 1, 1)) // LocalDate 객체로 생년월일 설정
                .phone("01012345678") // 하이픈 없는 전화번호
                .providerType(ProviderType.GOOGLE) // 인증 제공자 유형
                .providerId("123456") // 제공자 ID
                .imagePath("/path/to/image.jpg") // 이미지 경로
                .reviewExp(0) // 리뷰 경험치
                .point(0) // 포인트
                .activeStatus(ActiveStatus.ACTIVE) // 활성 상태
                .build());

        // 테스트 사용자의 ID를 사용하여 리프레시 토큰을 생성
        String refreshTokenString = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        Instant expiryDate = Instant.now().plusSeconds(3600); // 만료 시간 설정

        // 생성된 리프레시 토큰과 만료 시간을 설정하여 RefreshToken 인스턴스를 생성하고 저장
        RefreshToken refreshToken = new RefreshToken(testUser, refreshTokenString, expiryDate);
        refreshTokenRepository.save(refreshToken);

        CreateAccessTokenRequest request = new CreateAccessTokenRequest(refreshTokenString);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE) // 요청의 컨텐트 타입을 JSON으로 설정
                .content(requestBody)); // 요청 본문에 CreateAccessTokenRequest 객체를 JSON 문자열로 변환하여 전달

        // then
        resultActions
                .andExpect(status().isOk()) // HTTP 응답 상태 코드가 200(OK)인지 확인
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
        // 응답 JSON 본문에서 accessToken 필드가 비어 있지 않은지 확인
        // $.accessToken은 JSON 응답에서 accessToken 속성에 접근하기 위한 경로

    }
}

// 클라이언트가 유효한 리프레시 토큰을 제공했을 때,
// 서버가 새 액세스 토큰을 생성하고 올바르게 반환하는지를 검증하는 과정을 자동화