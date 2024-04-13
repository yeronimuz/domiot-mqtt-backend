FROM bitnami/java:17
MAINTAINER jeroenlankheet <yeronimuz@gmail.com>
ARG JAR_FILE
VOLUME /tmp
RUN mkdir -p /opt/domiot-backend
COPY build/libs/${JAR_FILE} /opt/mqtt-service/backend.jar
COPY src/main/resources/application.properties /opt/mqtt-backend/
CPPY logback-spring.xml /opt/mqtt-backend/
ENTRYPOINT ["java","-jar","/opt/mdomiot-backend/backend.jar"]
