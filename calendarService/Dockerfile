FROM eclipse-temurin:21-jre
WORKDIR /app
COPY calendarService/build/libs/calendarService-*.jar app.jar
EXPOSE 6709
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
