FROM openjdk:21-jdk-slim
VOLUME /tmp

COPY target/stats-http-service-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/stats_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

ENTRYPOINT ["java", "-jar", "/app.jar"]