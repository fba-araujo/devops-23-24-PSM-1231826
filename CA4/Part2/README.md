# Class Assignment 4 - Part 2: Dockerizing the Spring Basic Tutorial Application

## Introduction
In this tutorial, we will extend our Docker skills by setting up a containerized environment to execute a Gradle version of the Spring Basic Tutorial application. 
Unlike the previous assignment where we used Vagrant, this time we will use Docker and Docker Compose to create and manage our containers.

## Table of Contents
- [Step 1: Setting Up the Project](#step-1setting-up-the-project)
    - [Step 1.1: Create the Dockerfiles](#step-11-create-the-dockerfiles)
    - [Step 1.2: Create the docker-compose.yml File](#step-12-create-the-docker-composeyml-file)
    - [Step 1.3: Build and Run the Containers](#step-13-build-and-run-the-containers)
- [Step 2: Publishing the Images to Docker Hub](#step-2-publishing-the-images-to-docker-hub)
    - [Step 2.1: Tag the Docker Images](#step-21-tag-the-docker-images)
    - [Step 2.2: Push the Images to Docker Hub](#step-22-push-the-images-to-docker-hub)
- [Step 3: Check if the images are stored in the docker hub repository](#step-3-check-if-the-images-are-stored-in-the-docker-hub-repository)
- [Step 4: Working with Volumes](#step-4-working-with-volumes)
    - [Step 4.1: Create and Use a Volume for the Database](#step-41-create-and-use-a-volume-for-the-database)
- [Step 5: Commit Tag](#step-5-commit-tag)
- [Conclusion](#conclusion)
- [Exploring Heroku as an Alternative](#exploring-heroku-as-an-alternative)

## Step 1:Setting Up the Project

### Step 1.1: Create the Dockerfiles

#### Dockerfile for `web` (Tomcat and Spring Application)
Create a `Dockerfile` in a `web` directory in your project directory for the `web` service:
```Dockerfile
# Create a basic container with Java 17 and running Tomcat 10
FROM tomcat:10-jdk17-openjdk-slim

LABEL author="filipaba"

# Create a directory for the project and clone the repository there
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app/

# Update package list and install Git
RUN apt-get update && apt-get install -y git

# Clone the repository and navigate to the project directory
RUN git clone https://github.com/fba-araujo/devops-23-24-PSM-1231826.git .

# Navigate to the project directory
WORKDIR /usr/src/app/CA2/Part2/react-and-spring-data-rest-basic

# Change the permissions of the gradlew file to make it executable
RUN chmod +x gradlew

# Run the gradle build command
RUN ./gradlew build

# Copy the generated WAR file to the Tomcat webapps directory
RUN cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

# State the port that our application will run on
EXPOSE 8080

# Start Tomcat automatically when the container starts
CMD ["catalina.sh", "run"]
```

#### Dockerfile for `db` (H2 Database)
Create a `Dockerfile` in a `db` in your project directory for the `db` service:
```Dockerfile
FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk-headless && \
    apt-get install unzip -y && \
    apt-get install wget -y

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/

RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar

EXPOSE 8082
EXPOSE 9092

CMD ["java", "-cp", "./h2-1.4.200.jar", "org.h2.tools.Server", "-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers", "-ifNotExists"]
```

### Step 1.2: Create the docker-compose.yml File
Create a `docker-compose.yml` file in your project directory to define the services:
```yaml
version: '3.8'
services:
  web:
    build: web
    ports:
      - "8080:8080"
    networks:
      default:
        ipv4_address: 192.168.56.10
    depends_on:
      - "db"
  db:
    build: db
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - ./data:/usr/src/data-backup
    networks:
      default:
        ipv4_address: 192.168.56.11
networks:
  default:
    ipam:
      driver: default
      config:
        - subnet: 192.168.56.0/24
```

### Step 1.3: Build and Run the Containers
In your project directory build and run the Docker containers using Docker Compose:
```bash
docker compose build
docker compose up
```

## Step 2: Publishing the Images to Docker Hub
### Step 2.1: Tag the Docker Images
Tag the images before pushing them to Docker Hub:
```bash
docker tag part2-db filipaba/devops:ca4-part2-db
docker tag part2-web filipaba/devops:ca4-part2-web
```

### Step 2.2: Push the Images to Docker Hub
Push the tagged images to your Docker Hub repositories:
```bash
docker push filipaba/devops:ca4-part2-db
docker push filipaba/devops:ca4-part2-web
```

## Step 3: Check if the images are stored in the Docker Hub Repository
https://hub.docker.com/repository/docker/filipaba/devops/general


## Step 4: Working with Volumes
### Step 4.1: Create and Use a Volume for the Database
Use Docker exec to access the db container and copy the database file to the volume:
```bash
docker-compose exec db bash
```
#### Inside the container shell
> cp /usr/src/app/h2-1.4.200.jar /usr/src/data-backup
> exit

## Step 5: Commit Tag
At the end of this assignment, mark your repository with the tag ca4-part2.

## Conclusion
In this assignment, we successfully set up a containerized environment for a Spring application using Docker and Docker Compose. 
We created and managed two services (web and db), published the Docker images to Docker Hub, and utilized volumes to manage the database file. 
This process reinforced our understanding of Docker's capabilities in modern development workflows.

## Exploring Heroku as an Alternative
We'll explore one of the suggested alternatives: Heroku. 
While it's not a direct replacement for Docker, it provides an alternative method for deploying containers to the cloud.

Unlike many open-source applications we've used in this class, Heroku is a paid software. 
Therefore, we won't be fully implementing the alternative, but we'll walk through the step-by-step process.

### Step 1: Installation
To install Heroku, do the following:
```bash
choco install heroku-cli
```
### Step 2: Account Setup
Next, create an account on Heroku. Once that's done, log in to the account in the terminal using:
```bash
heroku login
```
### Step 3: Creating a New Heroku Application
Now, let's create a new Heroku application with a unique URL. Run the following command:
```bash
heroku create ca4-alternative
```
### Step 4: Pushing Docker Images to Heroku
To push Docker Images to Heroku, use the following commands:
```bash
heroku container:login
heroku container:push web --app ca4-alternative
heroku container:push db --app ca4-alternative
```

### Step 5: Making the Application Available
To make the application available on Heroku, release the containers with:
```bash
heroku container:release web db --app ca4-alternative
```

### Step 6: Accessing the Application
After completing these steps, access your application using the unique URL provided during the application creation process. 
In the browser, you'll be able to see the data about Frodo Baggins.
