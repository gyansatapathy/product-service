FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.1.13

MAINTAINER gyan.satapathy@yahoo.com

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]