FROM maven:3-openjdk-11-slim as build

WORKDIR /app
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package

FROM openjdk:11-slim as deploy

WORKDIR /app
COPY --from=build /app/target/**.jar ./app.jar
CMD ["java", "-jar", "app.jar"]
