FROM eclipse-temurin:21-jre
WORKDIR /app
COPY apiGateway/build/libs/apiGateway-*.jar app.jar
EXPOSE 6969
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
