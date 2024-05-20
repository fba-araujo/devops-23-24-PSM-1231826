# Class Assignment 4 - Part 1: Dockerizing the Chat Application

## Introduction
In this tutorial, we aim to practice Docker by creating Docker images and running containers using the chat application from CA2. 
Our objectives are to package and execute the chat server in a container, tag the image, publish it on Docker Hub, and connect to the chat server using a chat client running on the host computer. 
We will create two versions of our solution: one where the chat server is built inside the Dockerfile, and another where it is built on the host computer and copied into the Dockerfile.

## Table of Contents
- [Introduction](#introduction)
- [Version 1: Building the Chat Server Inside the Dockerfile](#version-1-building-the-chat-server-inside-the-dockerfile)
    - [Step 1: Create the Dockerfile](#step-1-create-the-dockerfile)
    - [Step 2: Build the Docker Image Tag and Push the Docker Image to Docker Hub](#step-2-build-the-docker-image-tag-it-and-push-it-automatically-)
    - [Step 4: Run the Docker Container](#step-3-run-the-docker-container)
- [Version 2: Building the Chat Server on the Host and Copying the Jar File into the Dockerfile](#version-2-building-the-chat-server-on-the-host-and-copying-the-jar-file-into-the-dockerfile)
    - [Step 1: Build the Chat Server on the Host](#step-1-build-the-chat-server-on-the-host)
    - [Step 2: Build the Docker Image Tag and Push the Docker Image to Docker Hub](#step-2-build-the-docker-image-tag-it-and-push-it-automatically)
    - [Step 3: Run the Docker Container](#step-3-run-the-docker-container-1)
- [Testing the Chat Application](#testing-the-chat-application)
- [Commit Tag](#commit-tag)
- [Conclusion](#conclusion)

## Version 1: Building the Chat Server Inside the Dockerfile

### Step 1: Create the Dockerfile
To start off this assignment, I had to create a Dockerfile (to create a docker image) to execute the chat server
in *CA2 - Part1*.

For this, I created a new *.Dockerfile* and named it *CA4Part1*.
```Dockerfile
# Use a base image with JDK
FROM gradle:jdk17 AS builder

LABEL author="filipaba"

# Set the working directory inside the container
WORKDIR /usr/src/devops

# Copy the application source code to the container
COPY ../../CA2/Part1/gradle_basic_demo /usr/src/devops/gradle_basic_demo

# Change the working directory to the copied project directory
WORKDIR /usr/src/devops/gradle_basic_demo

# Use sed to remove Windows-style line endings from gradlew
RUN sed -i 's/\r//' gradlew

# Build the chat server
RUN ./gradlew clean build

# Expose the port that the chat server listens on
EXPOSE 59001

# Define the command to run the chat server
CMD ["./gradlew", "runServer"]
```

### Step 2: Build the Docker Image, tag it and push it automatically 
- To run the image, install Docker Desktop, create an account and make sure it is running your computer.
```bash
docker build -f CA4/Part1/CA4Part1.Dockerfile -t filipaba/chat-server:version1 .
```

### Step 3: Run the Docker Container
- Run the Docker container from the image:
```bash 
docker run -d -p 59001:59001 filipaba/chat-server:version1
```

## Version 2: Building the Chat Server on the Host and Copying the Jar File into the Dockerfile
### Step 1: Build the Chat Server on the Host
- On your host machine, navigate to the root directory of your chat application and build the jar file:
```bash
cd CA2/Part1/gradle_basic_demo
./gradlew clean build
```

### Step 2: Create the Dockerfile
For this version of the Dockerfile, the build of the application must be done in your local machine and the
*.jar* file must be copied to the container in the Dockerfile.
To do this, I create a new Dockerfile named *CA4Part1v2.Dockerfile* and add the following:
```Dockerfile
# Use a base image with JDK 17
FROM openjdk:17-jdk

LABEL author="filipaba"

# Set the working directory inside the container
WORKDIR /usr/src/devops

# Copy the JAR file from the host machine to the container
COPY ../../CA2/Part1/gradle_basic_demo/build/libs/basic_demo-0.1.0.jar /usr/src/devops/basic_demo-0.1.0.jar

# Define the command to run the chat server
CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```

### Step 2: Build the Docker Image, tag it and push it automatically
- To run the image, install Docker Desktop, create an account and make sure it is running your computer.
```bash
docker build -f CA4/Part1/CA4Part1v2.Dockerfile -t filipaba/chat-server:version2 .
```

### Step 3: Run the Docker Container
- Run the Docker container from the image:
```bash 
docker run -d -p 59001:59001 filipaba/chat-server:version2
```

## Testing the Chat Application
After running the Docker container, test the chat application by connecting to the chat server running in the container from a chat client on your host machine.
- Use the chat client to connect to the server and verify that the chat application is working as expected.
```bash 
java -cp "CA2\Part1\gradle_basic_demo\build\libs\basic_demo-0.1.0.jar" basic_demo.ChatClientApp localhost 59001
```

## Commit Tag
At the end of this assignment, mark your repository with the tag ca4-part1.

## Conclusion
In this assignment, we practiced using Docker to create images and run containers for a chat application. 
We created two versions of the Docker setup: one where the chat server was built inside the Dockerfile, and another where it was built on the host machine and copied into the Dockerfile. 
We also learned how to tag images and push them to Docker Hub, as well as how to run and test the chat application in a Docker container.



