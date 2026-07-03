FROM eclipse-temurin:21-jre
WORKDIR /app
COPY notificationService/build/libs/notificationService-*.jar app.jar
EXPOSE 6704
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
