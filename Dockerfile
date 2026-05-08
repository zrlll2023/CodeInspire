# 多阶段构建
# 阶段1: 构建应用
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
RUN ./mvn/wrapper/maven-wrapper.jar -Dmaven.home=./.mvn wrapper:wrapper -o /app/.mvn/wrapper/maven-wrapper.jar
RUN ./mvn/wrapper/maven-wrapper.jar mvn dependency:go-offline -B --no-transfer-progress

COPY src src
RUN ./mvn/wrapper/maven-wrapper.jar package -DskipTests -B --no-transfer-progress

# 阶段2: 运行时镜像
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="CodeInspire Team"
LABEL description="CodeInspire Backend - AI-powered career advisor for CS students"

RUN apk add --no-cache tzdata curl

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseStringDeduplication"

WORKDIR /app

COPY --from=builder /app/target/codeinspire-backend-*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
