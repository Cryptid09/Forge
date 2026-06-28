# Multi-stage build for the DevOps Toolbox backend
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace
COPY backend/ ./backend/
WORKDIR /workspace/backend
RUN chmod +x gradlew && ./gradlew :bootstrap:bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=build /workspace/backend/bootstrap/build/libs/devops-toolbox.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
