FROM maven:3-jdk-8 AS build
WORKDIR /build
COPY . /build
RUN mvn package -B

FROM openjdk:8 AS deck-of-cards
WORKDIR /app
COPY --from=build /build/RestfulApi/target/restfulapi.jar /app
EXPOSE 8080
CMD ["java", "-jar", "restfulapi.jar"]