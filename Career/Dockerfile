FROM openjdk:21-jdk AS build
COPY . .

FROM openjdk:21-jdk-slim
COPY --from=build /target/career-0.0.1-SNAPCHAT.jar career.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","career.jar"]