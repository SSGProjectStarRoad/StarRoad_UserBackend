package com.ssg.starroad.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.review.DTO.ReviewRequestDTO;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void addReviewTest() throws Exception {
        // Given
        ReviewRequestDTO reviewRequest = new ReviewRequestDTO();
        reviewRequest.setContents("This is a test review.");

        Review mockReview = Review.builder().build(); // Review 엔티티의 생성자 및 setter를 적절히 사용하여 초기화
        mockReview.setContents("This is a test review.");
        given(reviewService.createReview(reviewRequest.toEntity())).willReturn(mockReview);

        // When & Then
        mockMvc.perform(post("/reviews/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated());
    }
}
