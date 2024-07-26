# Stage 1: Build the application
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /career
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /career
COPY --from=build /career/target/career-0.0.1-SNAPSHOT.jar career.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "career.jar"]
