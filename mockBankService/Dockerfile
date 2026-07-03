FROM eclipse-temurin:21-jre
WORKDIR /app
COPY mockBankService/build/libs/mockBankService-*.jar app.jar
EXPOSE 6707
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
