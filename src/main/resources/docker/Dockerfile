FROM bitnami/java:17
MAINTAINER yeronimuz <yeronimuz@gmail.com>
ARG JAR_FILE=domiot-mqtt-backend-0.1.1-SNAPSHOT.jar
VOLUME /tmp
RUN mkdir -p /opt/mqtt-backend
COPY build/libs/${JAR_FILE} /opt/mqtt-backend/mqtt-backend.jar
COPY src/main/resources/application.properties /opt/mqtt-backend/
COPY src/main/resources/logback-spring.xml /opt/mqtt-backend/

WORKDIR /opt/mqtt-backend

EXPOSE 7000

ENTRYPOINT ["java","-jar","/opt/mqtt-backend/mqtt-backend.jar", "-Dlogging.config=/opt/mqtt-backend/logback-spring.xml"]
