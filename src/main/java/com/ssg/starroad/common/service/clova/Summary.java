package com.ssg.starroad.common.service.clova;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class Summary {

    private final RestTemplate restTemplate;

    @Value("${naver.openapi.client-id}")
    private String CLOVA_API_KEY_ID;
    @Value("${naver.openapi.client}")
    private String CLOVA_API_KEY;

    // Summary API Option 설정
    private final String OPTION_LANGUAGE = "ko";
    private final String OPTION_MODEL = "general";
    private final Integer OPTION_TONE = 3;
    private final Integer OPTION_SUMMARY_COUNT = 2;

    private final String clovaUrl = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";

    public String getSummary(String content) {

        // 요청 설정
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLOVA_API_KEY_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLOVA_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 설정
        JSONObject body = new JSONObject();
        body.put("document", new JSONObject().put("content", content));
        body.put("option", new JSONObject()
                .put("language", OPTION_LANGUAGE)
                .put("model", OPTION_MODEL)
                .put("tone", OPTION_TONE)
                .put("summaryCount", OPTION_SUMMARY_COUNT));

        // 요청 객체 생성
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        // 요청 보내기
        String response = restTemplate.postForObject(clovaUrl, request, String.class);

        try {
            // 응답 파싱하기
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(response);

            // summary 값 가져오기
            return node.get("summary").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "요약 실패";
        }
    }
}
