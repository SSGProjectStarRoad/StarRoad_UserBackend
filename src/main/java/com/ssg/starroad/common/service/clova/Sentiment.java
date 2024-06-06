package com.ssg.starroad.common.service.clova;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.review.DTO.SentimentDTO;
import com.ssg.starroad.review.DTO.SentimentDetailDTO;
import com.ssg.starroad.review.entity.ReviewSentiment;
import com.ssg.starroad.review.enums.ConfidenceType;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Sentiment {

    private final RestTemplate restTemplate;

    @Value("${naver.openapi.client-id}")
    private String CLOVA_API_KEY_ID;
    @Value("${naver.openapi.client}")
    private String CLOVA_API_KEY;

    private final String clovaUrl = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";

    public SentimentDTO getSentiment(String content) {

        // 요청 설정
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLOVA_API_KEY_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLOVA_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 설정
        JSONObject body = new JSONObject();
        body.put("content", content);

        // 요청 객체 생성
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        // 요청 보내기
        String response = restTemplate.postForObject(clovaUrl, request, String.class);

        // 응답 받기 위해 준비
        List<ReviewSentiment> sentimentList = new ArrayList<>();
        List<SentimentDetailDTO> detailDTOList = new ArrayList<>();
        SentimentDTO build = null;
        try {
            // 응답 파싱하기
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(response);

            // document.sentiment 값 가져오기
            ConfidenceType confidenceType = ConfidenceType.valueOf(node.get("document").get("sentiment").asText().toUpperCase());

            // sentences 값 가져오기
            JsonNode sentences = node.get("sentences");

            // ReviewSentiment 객체로 변환
            for (JsonNode sentence : sentences) {
//                ReviewSentiment reviewSentiment = ReviewSentiment.builder()
//                        .content(sentence.get("content").asText())
//                        .confidence(ConfidenceType.valueOf(sentence.get("sentiment").asText().toUpperCase()))
//                        .offset(sentence.get("offset").asInt())
//                        .length(sentence.get("length").asInt())
//                        .highlightLength(sentence.get("highlights").get(0).get("length").asInt())
//                        .highlightOffset(sentence.get("highlights").get(0).get("offset").asInt())
//                        .confidenceStat(confidenceType)
//                        .build();

                // ReviewSentiment 객체 리스트에 추가
//                sentimentList.add(reviewSentiment);
                SentimentDetailDTO detailDTO = SentimentDetailDTO.builder()
                        .content(sentence.get("content").asText())
                        .confidence(ConfidenceType.valueOf(sentence.get("sentiment").asText().toUpperCase()))
                        .offset(sentence.get("offset").asInt())
                        .length(sentence.get("length").asInt())
                        .highlightLength(sentence.get("highlights").get(0).get("length").asInt())
                        .highlightOffset(sentence.get("highlights").get(0).get("offset").asInt())
                        .build();

                detailDTOList.add(detailDTO);
            }

            build = SentimentDTO.builder()
                    .documentConfidence(confidenceType)
                    .sentimentDetailDTOList(detailDTOList)
                    .build();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            return build;
//            return sentimentList;
        }
    }
}

