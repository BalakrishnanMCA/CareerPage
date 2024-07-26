# Stage 1: Build the application
FROM maven:3.8.6-openjdk-21 AS build
WORKDIR /Career
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /Career
COPY --from=build /career/target/Career-0.0.1-SNAPSHOT.jar career.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "career.jar"]
