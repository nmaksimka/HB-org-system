FROM eclipse-temurin:21-jre
WORKDIR /app
COPY groupService/build/libs/groupService-*.jar app.jar
EXPOSE 6702
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
