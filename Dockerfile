FROM openjdk:17-jdk-slim
COPY build/libs/*-0.0.1-SNAPSHOT.jar /app/springboot-app.jar
COPY src/main/resources/application.properties /app/application.properties
WORKDIR /app
CMD ["java", "-Dspring.config.location=file:/app/application.properties", "-jar", "springboot-app.jar"]
