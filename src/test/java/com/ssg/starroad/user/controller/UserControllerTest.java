package com.ssg.starroad.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.config.jwt.TokenProvider;
import com.ssg.starroad.user.dto.LoginRequest;
import com.ssg.starroad.user.dto.UserDTO;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.Gender;
import com.ssg.starroad.user.enums.ProviderType;
import com.ssg.starroad.user.repository.RefreshTokenRepository;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.RefreshTokenService;
import com.ssg.starroad.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @BeforeEach
    public void setup() {
// 사용자 데이터를 삭제하기 전에 해당 사용자의 모든 리프레시 토큰을 삭제
        userRepository.findByEmail("testuser@example.com")
                .ifPresent(user -> {
                    refreshTokenRepository.deleteByUserId(user.getId());  // 리프레시 토큰 먼저 삭제
                    userRepository.delete(user);  // 이후 사용자 삭제
                });

        // 테스트용 사용자 데이터 생성
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("Valid123$!");
        userDTO.setName("TestUser");
        userDTO.setNickname("Tester");
        userDTO.setGender(Gender.MALE);
        userDTO.setBirth(LocalDate.of(1990, 1, 1));
        userDTO.setPhone("01012345678");
        userDTO.setProviderType(ProviderType.GOOGLE);
        userDTO.setProviderId("providerId123");
        userDTO.setImagePath("/path/to/image.jpg");
        userDTO.setReviewExp(0);
        userDTO.setPoint(0);
        userDTO.setActiveStatus(ActiveStatus.ACTIVE);

        userService.save(userDTO);
        setupSecurityContext("testuser@example.com");
    }
    private void setupSecurityContext(String email) {
        UserDetails userDetails = userService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Test
    public void testLoadUserByUsername() {
        UserDetails userDetails = userService.loadUserByUsername("testuser@example.com");
        assertNotNull(userDetails);
        assertEquals("testuser@example.com", userDetails.getUsername());
    }
    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser@example.com", "Valid123$!");
        String loginContent = objectMapper.writeValueAsString(loginRequest);

        logger.info("Sending login request for {}", loginRequest.getEmail());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(header().exists("Authorization"))
                .andReturn();

        logger.info("Login test passed for {}", loginRequest.getEmail());
        logger.info("Response: {}", result.getResponse().getContentAsString());
    }

    @Test
    public void whenInvalidUser_thenUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("wrong1@google.com", "password@456");

        // 특정 이메일과 패스워드 조합에 대한 결과를 명시적으로 설정
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
