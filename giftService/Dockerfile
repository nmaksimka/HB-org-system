FROM eclipse-temurin:21-jre
WORKDIR /app
COPY giftService/build/libs/giftService-*.jar app.jar
EXPOSE 6702
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
