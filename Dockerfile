FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=Core/target/core.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]