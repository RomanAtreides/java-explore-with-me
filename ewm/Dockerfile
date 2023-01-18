FROM amazoncorretto:11-alpine-jdk
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
WORKDIR /app
COPY target/*.jar ewm.jar
ENTRYPOINT ["java","-jar","./ewm.jar"]
