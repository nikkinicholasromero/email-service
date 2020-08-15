# Email Service

#### Build the JAR using Maven
``
mvn clean install
``

#### Build the Docker Image using the Dockerfile
``
docker build --tag email-service-container:0.1 .
``

#### Start a Docker Container using the Docker Image
``
docker run -d -t -p 127.0.0.1:8080:8080 --name email-service email-service-container:0.1
``

#### Connect to the bash of Docker Container 
``
docker exec -it email-service /bin/bash
``

#### Start the Java Application inside the Docker Container
``
java -jar -Dspring.profiles.active=dev email-service-1.0-SNAPSHOT.jar
``