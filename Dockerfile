FROM openjdk:11

WORKDIR /usr/src/myapp
COPY ./target/TODO-0.0.1-SNAPSHOT.jar /usr/src/myapp

expose 8080:8080

CMD ["java", "-jar", "TODO-0.0.1-SNAPSHOT.jar"]