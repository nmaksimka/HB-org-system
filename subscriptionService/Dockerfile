FROM eclipse-temurin:21-jre
WORKDIR /app
COPY subscriptionService/build/libs/subscriptionService-*.jar app.jar
EXPOSE 6703
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
