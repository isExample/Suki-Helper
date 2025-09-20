# =================================================================
# 애플리케이션 빌드
# =================================================================
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /workspace

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

COPY src ./src

RUN chmod +x ./gradlew

# Gradle을 사용하여 애플리케이션을 빌드하고 실행 가능한 JAR 파일을 생성
# --no-daemon 옵션은 CI/CD 환경에서 권장
RUN ./gradlew bootJar --no-daemon


# =================================================================
# 최종 이미지 생성
# =================================================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

# 애플리케이션이 사용하는 포트(기본 8080)를 외부에 노출
EXPOSE 8080

# 컨테이너가 시작될 때 애플리케이션을 실행하는 명령어를 설정
ENTRYPOINT ["java", "-jar", "app.jar"]
