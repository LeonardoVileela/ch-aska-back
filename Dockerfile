FROM adoptopenjdk/openjdk15:ubi
ARG JAR_FILE=build/libs/ch-aska-back-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
