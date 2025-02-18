FROM openjdk:17-jdk-slim

COPY target/spring-localstack-0.0.1-SNAPSHOT.jar /app/spring-localstack.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/spring-localstack.jar"]