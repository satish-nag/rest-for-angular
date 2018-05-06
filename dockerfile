FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} rest-for-angular.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/rest-for-angular.jar"]