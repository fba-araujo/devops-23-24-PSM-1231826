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