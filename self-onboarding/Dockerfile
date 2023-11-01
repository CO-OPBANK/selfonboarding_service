FROM openjdk:11-jdk-alpine
COPY settings.xml /root/.m2/settings.xml
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ./target/self-onboarding-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
