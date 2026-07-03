FROM eclipse-temurin:21-jre
WORKDIR /app
COPY apiGateway/build/libs/apiGateway-*.jar app.jar
EXPOSE 1488
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
