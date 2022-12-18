FROM amazoncorretto:11-alpine-jdk
WORKDIR /app
COPY target/*.jar stats.jar
ENTRYPOINT ["java","-jar","./stats.jar"]
