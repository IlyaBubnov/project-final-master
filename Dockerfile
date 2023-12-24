FROM openjdk
COPY resources ./resources
COPY target/*.jar /app/jira-1.0.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/jira-1.0.jar"]