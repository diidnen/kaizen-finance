# 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制 pom.xml 并下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 生产阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 复制构建的 jar 文件
COPY --from=builder /app/target/*.jar app.jar

# 创建非 root 用户
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

