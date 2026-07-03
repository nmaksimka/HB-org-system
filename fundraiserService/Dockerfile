FROM eclipse-temurin:21-jre
WORKDIR /app
COPY fundraiserService/build/libs/fundraiserService-*.jar app.jar
EXPOSE 6707
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
