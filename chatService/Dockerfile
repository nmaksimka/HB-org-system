FROM eclipse-temurin:21-jre
WORKDIR /app
COPY chatService/build/libs/chatService-*.jar app.jar
EXPOSE 6705
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
