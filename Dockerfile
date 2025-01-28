FROM openjdk:21-jre-slim

LABEL maintainer="wnstj0614@naver.com"
LABEL description="On's Backend API 서버입니다."

WORKDIR /opt

COPY *.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]