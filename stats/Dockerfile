FROM amazoncorretto:11-alpine-jdk
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
WORKDIR /app
COPY target/*.jar stats.jar
ENTRYPOINT ["java","-jar","./stats.jar"]
