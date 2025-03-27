FROM openjdk:17

WORKDIR /app

ARG JAR_FILE="app-external-api/build/libs/*.jar"
ARG PROFILE=dev

COPY ${JAR_FILE} dongsan-server.jar

ENV PROFILE=${PROFILE}

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-Duser.timezone=Asia/Seoul", "dongsan-server.jar"]