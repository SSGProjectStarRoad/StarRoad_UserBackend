package com.ssg.starroad.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RootConfig {

    @Bean  // 해당 메서드의 실행 결과로 반환 된 객체를 스프링의 빈으로 등록시키는 역할
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Autowired
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory(); // 기본적인 HTTP 연결 설정을 제공하는 클래스
        requestFactory.setConnectTimeout(5000); // 연결 제한시간 5초
        requestFactory.setReadTimeout(5000); // 읽기 제한시간 5초

        restTemplate.setRequestFactory(requestFactory); // RestTemplate에 위에서 설정한 requestFactory를 설정

        return restTemplate;
    }

}
