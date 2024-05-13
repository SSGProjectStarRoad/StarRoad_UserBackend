package com.ssg.starroad.common.util;

// OAuth2 인증 플로우에서 쿠키를 사용하는 주된 이유는 인증 과정에서의 상태 관리와 보안을 강화하기 위해
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

    // 새로운 쿠키를 생성하고 클라이언트에게 전송
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); // 모든 경로에서 사용
        cookie.setHttpOnly(true);  // JavaScript를 통한 접근 방지
        cookie.setSecure(true);    // HTTPS 통신에서만 쿠키 전송
        cookie.setMaxAge(maxAge); // 쿠키의 생명 주기 제어
        response.addCookie(cookie); // 응답에 쿠키를 추가하여 클라이언트에게 전송
    }
    // HTTPOnly와 Secure 플래그를 가지게 설정하여 XSS와 데이터 중간자 공격을 방지

    // 지정된 이름의 쿠키를 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        // 요청에서 쿠키 배열을 가져옴 쿠키가 없으면 메소드 종료
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue(""); // 해당 쿠키의 값을 빈 문자열로 설정
                cookie.setPath("/");
                cookie.setMaxAge(0); // 0으로 설정하여 즉시 만료되게
                response.addCookie(cookie);
            }
            // 변경된 쿠키를 응답에 추가하여 클라이언트에게 전송, 쿠키를 삭제
        }
    }

    // 객체를 직렬화한 후 Base64 인코딩된 문자열로 변환하여 반환
    // dbj : 직렬화할 객체
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }
    // SerializationUtils를 사용하여 객체를 바이트 배열로 직렬화
    // 직렬화된 데이터를 Base64 인코딩하여 안전하게 문자열 형태로 변환
    // HTTP 헤더나 쿠키 값으로 사용될 때 유용

    // 쿠키에 저장된 Base64 인코딩된 문자열을 객체로 역직렬화
    // cookie: 역직렬화할 데이터를 포함하고 있는 쿠키
    // cls: 반환될 객체의 클래스 타입
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
    // 쿠키에서 값을 가져와 Base64 디코딩을 수행하여 바이트 배열로 변환
    // SerializationUtils를 사용하여 바이트 배열을 객체로 역직렬화
    // 역직렬화된 객체를 지정된 클래스 타입으로 캐스팅하여 반환
}
