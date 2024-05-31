package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.enums.ConfidenceType;
import com.ssg.starroad.shop.entity.Store;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDTO {
    private String userNickname;
    private Long id;
    private Long userId;
    private String userEmail;
    private String imagePath;
    private boolean following; //팔로우 상태
    private Long storeId;
    private boolean visible;
    private Long likeCount;
    private String paymentNum;
    private String contents;
    private String summary;
    private List<ReviewImageDTO> reviewImages; // 리뷰 이미지 리스트 추가
    private List<ReviewFeedbackDTO> reviewFeedbacks; // 리뷰 피드백 추가
    private Long reviewcount; // 리뷰 갯수
    private boolean isLiked;  // 내가 추가한 필드 choims 24/05/23
    private LocalDateTime createDate;
    private ConfidenceType confidence;

    private Store store;
    private String shopName;
    private String purchaseDate;
    private String selectedTime;
    private List<MultipartFile> uploadedImages;
    private SurveyData surveyData;
    private String reviewText;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SurveyData {
        private List<String> essential;
        private List<String> optional;
    }

    public String getCombinedSurveyData() {
        return this.surveyData == null ? "" : this.surveyData.getEssential() + "," + this.surveyData.getOptional();
    }
}
