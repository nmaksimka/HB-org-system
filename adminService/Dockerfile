FROM eclipse-temurin:21-jre
WORKDIR /app
COPY adminService/build/libs/adminService-*.jar app.jar
EXPOSE 6710
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
