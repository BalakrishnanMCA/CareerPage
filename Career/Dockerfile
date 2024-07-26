FROM openjdk:21-jdk AS builder
WORKDIR /app
COPY Career/pom.xml .
RUN apt update && apt install -y maven
RUN mvn package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
