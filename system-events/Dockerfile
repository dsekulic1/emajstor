FROM azul/zulu-openjdk-alpine:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8085
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
