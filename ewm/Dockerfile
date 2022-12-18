FROM amazoncorretto:11-alpine-jdk
WORKDIR /app
COPY target/*.jar ewm.jar
ENTRYPOINT ["java","-jar","./ewm.jar"]
