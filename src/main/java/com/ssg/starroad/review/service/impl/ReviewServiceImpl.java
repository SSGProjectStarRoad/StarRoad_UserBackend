package com.ssg.starroad.review.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.common.service.S3Uploader;
import com.ssg.starroad.common.service.clova.Sentiment;
import com.ssg.starroad.review.DTO.*;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewImage;
import com.ssg.starroad.review.repository.ReviewFollowRepository;
import com.ssg.starroad.review.repository.ReviewImageRepository;
import com.ssg.starroad.review.repository.ReviewLikeRepository;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewFeedbackService;
import com.ssg.starroad.review.service.ReviewImageService;
import com.ssg.starroad.review.service.ReviewSentimentService;
import com.ssg.starroad.review.service.ReviewService;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewFollowRepository ReviewFollowRepository;
    private final RestTemplate restTemplate;
    private final ReviewImageService reviewImageService;
    private final ReviewFeedbackService reviewFeedbackService;
    private final ReviewFollowRepository reviewFollowRepository;
    private final UserRepository userRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final StoreRepository storeRepository;
    private final S3Uploader s3Uploader;
    private final Sentiment sentiment;
    private final ReviewSentimentService reviewSentimentService;


    @Value("${naver.openapi.ocr.secret-key}")
    private String secretKey;

    @Value("${naver.openapi.summary.key}")
    private String summaryKey;

    @Value("${naver.openapi.summary.key-id}")
    private String summaryKeyID;


    @Override
    public Long countReviewsByUserId(Long userId) {
        return reviewRepository.countByUserId(userId);
    }

    @Override
    @Transactional
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    @Transactional
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found for this id :: " + id));

        review.updateContents(reviewDetails.getContents());
        review.updateSummary(reviewDetails.getSummary());
        review.updateVisibility(reviewDetails.isVisible());
        review.updateLikeCount(reviewDetails.getLikeCount());

        final Review updatedReview = reviewRepository.save(review);
        return updatedReview;
    }


    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Review with id " + id + " not found"));
        reviewRepository.delete(review);
    }

    // OCR API를 호출하는 메서드 예시
    public ResponseEntity<String> callOcrApi(MultipartFile imageFile) throws IOException {
        // OCR API 엔드포인트
        String ocrApiUrl = "https://deqz3tj602.apigw.ntruss.com/custom/v1/30975/c4e5e9a47173ca497d023af6f322225681d8d3812a8648c4b32ba29bc7112a59/document/receipt";

        // OCR API에 요청을 보낼 때 필요한 headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", secretKey);

        // 이미지 파일을 Base64 인코딩
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());

        // 요청 바디 생성
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("format", "png");
        imageMap.put("data", base64Image);
        imageMap.put("name", "testReceipt");
//        imageMap.put("url", null); // null 값을 허용하는 HashMap 사용

        List<Map<String, Object>> images = new ArrayList<>();
        images.add(imageMap);

        Map<String, Object> body = new HashMap<>();
        body.put("version", "V2");
        body.put("requestId", "ocrCheck");
        body.put("timestamp", System.currentTimeMillis());
        body.put("images", images);
//        body.put("lang", "ko");
//        body.put("resultType", "string");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // OCR API에 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(ocrApiUrl, entity, String.class);
        System.out.println("callOcrApi : " + response);
        return response;
    }

    @Override
    public ResponseReviewDTO findAllReview(String userEmail, int pageNo, int pageSize) {
        // 최신순으로 정렬된 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "likeCount"));
        // 정렬된 Pageable 객체를 사용하여 리뷰 페이지 가져오기
        Page<Review> reviewPage = reviewRepository.findAll(pageable);
        Long userId = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")).getId();
//        Long totalReviewCount = reviewRepository.countByStoreId(id);

        List<ReviewDTO> reviewDTOList = reviewPage.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                    List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = countReviewsByUserId(review.getUser().getId());
                    boolean isLiked = reviewLikeRepository.existsByUser_IdAndReview_Id(userId, review.getId());

                    return ReviewDTO.builder()
                            .id(review.getId())
                            .userId(review.getUser().getId())
                            .userNickname(review.getUser().getNickname())
                            .imagePath(review.getUser().getImagePath())
                            .storeId(review.getStore().getId())
                            .visible(review.isVisible())
                            .createDate(review.getCreatedAt())
                            .likeCount(review.getLikeCount())
                            .contents(review.getContents())
                            .summary(review.getSummary())
                            .confidence(review.getConfidence())
                            .reviewcount(userReviewCount)
                            .reviewImages(reviewImageDTOs)
                            .reviewFeedbacks(reviewFeedbackDTOs)
                            .store(review.getStore())
                            .build();
                })
                .collect(Collectors.toList());
        List<ReviewDTO> sortedList = reviewDTOList.stream()
                .sorted(Comparator.comparingLong(ReviewDTO::getLikeCount).reversed())
                .collect(Collectors.toList());

        return ResponseReviewDTO.builder()
                .reviews(reviewDTOList)
                .pageNumber(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .hasNext(reviewPage.hasNext())
                .build();
    }

    @Override
    public ResponseReviewDTO findFollowingReview(String userEmail, int pageNo, int pageSize) {
        // 최신순으로 정렬된 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        // 정렬된 Pageable 객체를 사용하여 리뷰 페이지 가져오기

        // 사용자 이메일을 통해 사용자 ID를 조회합니다.
        Long userId = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")).getId();

        List<Long> toUserIds = reviewFollowRepository.findToUserIdsByFromUserId(userId);

        Page<Review> reviewPage = reviewRepository.findAllByUserIds(toUserIds, pageable);


//        Page<Review> reviewPage = reviewFollowRepository.findToUserIdsByFromUserId(id).stream().map(followId -> reviewRepository.findAllWithPageByUserId(followId, pageable)).toList().stream().findFirst().orElse(null);

        List<ReviewDTO> reviewDTOList = reviewPage.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                    List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = countReviewsByUserId(review.getUser().getId());
                    boolean isLiked = reviewLikeRepository.existsByUser_IdAndReview_Id(userId, review.getId());
                    return ReviewDTO.builder()
                            .id(review.getId())
                            .userId(review.getUser().getId())
                            .userNickname(review.getUser().getNickname())
                            .imagePath(review.getUser().getImagePath())
                            .storeId(review.getStore().getId())
                            .visible(review.isVisible())
                            .createDate(review.getCreatedAt())
                            .likeCount(review.getLikeCount())
                            .contents(review.getContents())
                            .summary(review.getSummary())
                            .confidence(review.getConfidence())
                            .reviewcount(userReviewCount)
                            .reviewImages(reviewImageDTOs)
                            .reviewFeedbacks(reviewFeedbackDTOs)
                            .isLiked(isLiked)
                            .store(review.getStore())
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseReviewDTO.builder()
                .reviews(reviewDTOList)
                .pageNumber(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .hasNext(reviewPage.hasNext())
                .build();
    }

    @Override
    public String makeSummary(String content) {
        System.out.println("makeSummary 진입");
        // OCR API 엔드포인트
        String summaryApiUrl = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";

        // OCR API에 요청을 보낼 때 필요한 headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-APIGW-API-KEY-ID", summaryKeyID);
        headers.set("X-NCP-APIGW-API-KEY", summaryKey);

        // 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("document", Map.of(
                "title", "",
                "content", content
        ));
        body.put("option", Map.of(
                "language", "ko",
                "model", "general",
                "tone", 3,
                "summaryCount", 3
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        System.out.println("before makeSummary : " + entity);
        // 텍스트 요약 API에 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(summaryApiUrl, entity, String.class);
        System.out.println("makeSummary : " + response.getBody());

        // JSON 응답 문자열
        ObjectMapper objectMapper = new ObjectMapper();
        String summary = null;
        try {
            // JSON 문자열을 JsonNode로 변환
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // summary 값 추출
            JsonNode summaryNode = rootNode.path("summary");
            summary = summaryNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // "summary" 키에 해당하는 값을 가져옴
        return summary;
    }

    @Override
    public ResponseEntity<String> saveSurvey(ReviewDTO reviewDTO, List<MultipartFile> uploadedImages) {
        System.out.println("reviewSelectionDTO toString : " + reviewDTO.toString());

        User user = userRepository.findByEmail(reviewDTO.getUserEmail()).orElseThrow(() -> new IllegalArgumentException("User not found for the userEmail: " + reviewDTO.getUserEmail()));
        System.out.println("sumbitSurvey user : " + user.toString());
        // Store 엔티티가 아직 저장되지 않은 경우 저장
        System.out.println("reviewDTO getShopName : " + reviewDTO.getShopName());
        Store store = storeRepository.findByName(reviewDTO.getShopName()).orElseThrow(() -> new IllegalArgumentException("Store not found for the shop name: " + reviewDTO.getShopName()));
        System.out.printf("Store Id : " + store.getId());

        String summary = makeSummary(reviewDTO.getContents());

        SentimentDTO dto = sentiment.getSentiment(reviewDTO.getContents());

        // Review 엔티티 생성 및 저장
        Review savedReview = createReview(Review.builder()
                .user(user)
                .store(store)
                .confidence(dto.getDocumentConfidence())
                .paymentNum(reviewDTO.getPaymentNum())
                .contents(reviewDTO.getContents())
                .summary(summary)
                .visible(true)
                .likeCount(0L)
                .build());

        List<ReviewSentimentDTO> sentimentDTOList = dto.getSentimentDetailDTOList().stream().map(x -> ReviewSentimentDTO.builder()
                .reviewId(savedReview.getId())
                .content(x.getContent())
                .totalOffset(x.getOffset())
                .totalLength(x.getLength())
                .confidence(x.getConfidence())
                .highlightOffset(x.getHighlightOffset())
                .highlightLength(x.getHighlightLength())
                .build()).toList();

        List<ReviewSentimentDTO> reviewSentimentDTOList = sentimentDTOList.stream().map(reviewSentimentDTO -> {
            System.out.println("reviewSentimentDTO : " + reviewSentimentDTO.toString());
            return reviewSentimentService.createReviewSentiment(reviewSentimentDTO);
        }).toList();

        System.out.println("리뷰 생성 완료");
        Long reviewId = savedReview.getId();
        System.out.println("submitSurvey reviewId : " + reviewId);

        List<String> imageUrls = new ArrayList<>();
        if (uploadedImages != null && !uploadedImages.isEmpty()) {
            imageUrls = s3Uploader.upload(uploadedImages.toArray(new MultipartFile[uploadedImages.size()]), "ssg/reviews");
        }

        System.out.println("================================================================");
        for (String imageUrl : imageUrls) {
            System.out.println(imageUrl);
        }

        // surveyData를 합쳐서 ReviewFeedbackDTO 생성
        String combinedSurveyData = reviewDTO.getCombinedSurveyData();
        ReviewFeedbackDTO reviewFeedbackDTO = ReviewFeedbackDTO.builder()
                .id(user.getId())
                .reviewId(reviewId)
                .reviewFeedbackSelection(combinedSurveyData)
                .build();

        System.out.println("submitSurvey reviewFeedbackDTO ID : " + reviewFeedbackDTO.getId());

        imageUrls.forEach(url -> reviewImageRepository.save(ReviewImage.builder()
                .review(savedReview)
                .imagePath(url)
                .build()));

        reviewFeedbackService.addReviewFeedback(reviewFeedbackDTO);

        return ResponseEntity.ok("설문이 성공적으로 제출되었습니다!");
    }

    public ResponseReviewDTO getUserReview(String email, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        Page<Review> reviewPage = reviewRepository.findAllByUserIds(Collections.singletonList(userId), pageable);

        List<ReviewDTO> reviewDTOList = reviewPage.stream()
                .map(review -> {
                    List<ReviewImageDTO> reviewImageDTOs = reviewImageService.getReviewImages(review.getId());
                    List<ReviewFeedbackDTO> reviewFeedbackDTOs = reviewFeedbackService.getReviewFeedback(review.getId());
                    Long userReviewCount = countReviewsByUserId(review.getUser().getId());

                    return ReviewDTO.builder()
                            .id(review.getId())
                            .userId(review.getUser().getId())
                            .userNickname(review.getUser().getNickname())
                            .imagePath(review.getUser().getImagePath())
                            .storeId(review.getStore().getId())
                            .visible(review.isVisible())
                            .createDate(review.getCreatedAt())
                            .likeCount(review.getLikeCount())
                            .contents(review.getContents())
                            .summary(review.getSummary())
                            .confidence(review.getConfidence())
                            .reviewcount(userReviewCount)
                            .reviewImages(reviewImageDTOs)
                            .reviewFeedbacks(reviewFeedbackDTOs)
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseReviewDTO.builder()
                .reviews(reviewDTOList)
                .pageNumber(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .hasNext(reviewPage.hasNext())
                .build();

    }
}
