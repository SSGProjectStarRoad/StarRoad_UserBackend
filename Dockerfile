# 1. OpenJDK 베이스 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 복사
COPY build/libs/*-0.0.1-SNAPSHOT.jar /app/springboot-app.jar

# 4. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/springboot-app.jar"]
