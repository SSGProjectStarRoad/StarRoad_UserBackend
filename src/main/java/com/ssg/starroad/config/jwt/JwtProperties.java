package com.ssg.starroad.config.jwt;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
// 자바 클래스에 프로퍼티 값을 가져와서 사용. 프로퍼티들을 자동으로 바인딩.
// prefix = "jwt"는 프로퍼티 이름이 jwt.xxx 형식임을 의미

public class JwtProperties {
    @NotBlank
    private String secretKey;
    private String issuer;
}
