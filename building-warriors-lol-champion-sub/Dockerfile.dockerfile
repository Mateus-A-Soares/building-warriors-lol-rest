## Builder Image
FROM gradle:7.0.2-jdk11 AS builder
COPY src /usr/src/app/src
COPY build.gradle /usr/src/app
COPY gradle.properties /usr/src/app
COPY micronaut-cli.yml /usr/src/app
WORKDIR /usr/src/app
RUN gradle build -x test --no-daemon

## Runner Image
FROM openjdk:11
COPY --from=builder /usr/src/app/build/libs/app-0.1-all.jar /usr/app/app.jar
EXPOSE 8080-8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
