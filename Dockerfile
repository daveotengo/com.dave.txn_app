FROM openjdk:8
ADD target/docker-txn_app.jar docker-txn_app.jar
EXPOSE 2021
ENTRYPOINT ["java", "-jar", "docker-txn_app.jar"]

