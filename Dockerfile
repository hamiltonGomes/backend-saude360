FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
COPY . .
RUN mvn clean package -X -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build ./app/target/*.jar ./backend-saude360*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-saude360*.jar"]
