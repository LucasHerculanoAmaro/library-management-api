FROM eclipse-temurin:24-jre

WORKDIR /app

COPY target/library_management_api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]