FROM eclipse-temurin:21-jre
WORKDIR /app
COPY userService/build/libs/userService-*.jar app.jar
EXPOSE 6700
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
