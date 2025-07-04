FROM openjdk:17

WORKDIR /app

ARG JAR_FILE="dongsan-external-api/build/libs/*.jar"
ARG PROFILE=dev

COPY ${JAR_FILE} dongsan-api-server.jar

ENV PROFILE=${PROFILE}

ENTRYPOINT ["java", \
  "-javaagent:/pinpoint-agent/pinpoint-bootstrap-2.5.3.jar", \
  "-Dpinpoint.agentId=dongsan-agent-id", \
  "-Dpinpoint.applicationName=dongsan", \
  "-Dspring.profiles.active=${PROFILE}", \
  "-Duser.timezone=Asia/Seoul", \
  "-jar", "dongsan-api-server.jar"]
