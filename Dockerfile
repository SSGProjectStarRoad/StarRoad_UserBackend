# 1. OpenJDK 베이스 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 복사
COPY build/libs/*-0.0.1-SNAPSHOT.jar /app/springboot-app.jar
COPY src/main/resources/application.properties /app/config/application.properties

# 4. 애플리케이션 실행
CMD ["java", "-jar", "springboot-app.jar", "--spring.config.location=classpath:/config/application.properties"]