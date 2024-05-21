FROM openjdk:21-jdk

RUN mkdir /app
WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/application.jar
EXPOSE 8080

RUN chown -R 1001:1001 /app

ENTRYPOINT java -jar /app/application.jar