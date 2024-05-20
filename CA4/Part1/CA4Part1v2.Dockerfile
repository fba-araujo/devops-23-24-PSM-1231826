# Use a base image with JDK 17
FROM openjdk:17-jdk

LABEL author="filipaba"

# Set the working directory inside the container
WORKDIR /usr/src/devops

# Copy the JAR file from the host machine to the container
COPY ../../CA2/Part1/gradle_basic_demo/build/libs/basic_demo-0.1.0.jar /usr/src/devops/basic_demo-0.1.0.jar

# Define the command to run the chat server
CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]

