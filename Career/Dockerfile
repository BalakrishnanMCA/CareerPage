FROM openjdk:21-jdk AS builder
WORKDIR /app
COPY pom.xml .
RUN apk add --no-cache maven
RUN mvn package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
