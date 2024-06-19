# Class Assignment 5 - Jenkins Pipelines with Gradle and Spring Boot

## Introduction
In this tutorial, we will use Jenkins with docker.
Jenkins is an open-source tool used to implement continuous integration/continuous delivery workflows, called pipelines. 
In these pipelines, you have complete control of the steps and stages, which you can create to run tests, generate Javadocs, check test coverage, etc.

## Table of Contents
- [Step 1: Jenkins Setup](#step-1-jenkins-setup)
  - [Step 1.1: Creating the Pipeline Example](#step-11-creating-the-pipeline-example)
- [Step 2: Creating the Pipeline for CA5 - Part1](#step-2-creating-the-pipeline-for-ca5---part1)
- [Step 3: Creating the Pipeline for CA5 - Part2](#step-3-creating-the-pipeline-for-ca5---part2)
  - [Step 3.1: Then, in the CA5 directory, create a Dockerfile with the following:](#step-31-then-in-the-ca5-directory-create-a-dockerfile-with-the-following)
  - [Step 3.2: Create a docker-compose.yml file with the following:](#step-32-create-a-docker-composeyml-file-with-the-following)
  - [Step 3.3: Then, create a new pipeline in Jenkins with the following script:](#step-33-then-create-a-new-pipeline-in-jenkins-with-the-following-script)
- [Step 4: Tag Repository](#step-4-tag-repository)
- [Conclusion](#conclusion)

## Step 1: Jenkins Setup
To install Jenkins using Docker, first, make sure you have Docker Installed the Docker application is running. 
Then, run the following command:
```bash
docker run -d -p 8080:8080 -p 50000:50000 -v jenkins-data:/var/jenkins_home --name=jenkins jenkins/jenkins:lts-jdk17
```

In localhost:8080, fill in the password field. To find this password, go to the jenkins Docker container and open the Files tab. 
There, go into the /var/jenkins_home/secrets/initialAdminPassword directory and copy the password. 
Back in localhost:8080, follow the rest of the setup steps. Choosing the suggested plugins is enough. Creating an admin user is also necessary.

### Step 1.1: Creating the Pipeline Example
In the Jenkins main page, select New Item, name it example and select the pipeline option. 
Then, configure the pipeline by selecting pipeline on the left of the screen.
Change the github repository to your own repository and add your credentials.
In the script section, add the following:
```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out...'
                git 'https://github.com/fba-araujo/devops-23-24-PSM-1231826.git'
                dir('CA2/Part1/gradle_basic_demo') {
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                sh './gradlew clean build'
                dir('CA2/Part1/gradle_basic_demo') {
                }
            }
        }
        stage('Archiving') {
            steps {
                echo 'Archiving...'
                archiveArtifacts 'build/distributions/*'
                dir('CA2/Part1/gradle_basic_demo') {
                }
            }
        }
    }
}
```

After saving, run the pipeline by clicking on Build Now. In the Console Output tab, you can see the pipeline steps and, by scrolling to the end, confirm that the build was successful.

## Step 2: Creating the Pipeline for CA5 - Part1
To create the pipeline for CA5 - Part1, create a Jenkinsfile with the following stages:
- Checkout: Checkout the code from the repository.
- Assemble: Compile and produce archive files with the application (without running tests).
- Test: Execute unit tests and publish the results using the junit step.
- Archive: Archive the generated files from the Assemble stage.

In the Jenkins main page, select New Item, name it `CA5-Part1` and select the pipeline option. 
Then, configure the pipeline by selecting pipeline on the left of the screen.
In the configuration, select the Pipeline script from SCM option and add the repository URL and the Jenkinsfile path.
The path should be `CA5/Part1/Jenkinsfile`.
Check if your working branch is `/main` or `/master` and save the configuration.

```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/fba-araujo/devops-23-24-PSM-1231826.git'
                dir('CA2/Part1/gradle_basic_demo') {
                }
            }
        }
        stage('Assemble') {
            steps {
                dir('CA2/Part1/gradle_basic_demo') {
                    sh 'chmod +x gradlew'
                    sh './gradlew assemble'
                }
            }
        }
        stage('Test') {
            steps {
                dir('CA2/Part1/gradle_basic_demo') {
                    sh './gradlew test'
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
        stage('Archive') {
            steps {
                dir('CA2/Part1/gradle_basic_demo') {
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                }
            }
        }
    }
}
```

In the Jenkins dashboard, you can click to build the pipeline.
And in the Console Output tab, you can see the pipeline steps and, by scrolling to the end, confirm that the build was successful.

## Step 3: Creating the Pipeline for CA5 - Part2
To create the pipeline for CA5 - Part2, create a Jenkinsfile capable of running other Docker containers. 
For this, run the following commands (referenced in https://www.jenkins.io/doc/book/installing/docker/):
```bash
docker network create jenkins
docker run --name jenkins-docker --rm --detach --privileged --network jenkins --network-alias docker --env DOCKER_TLS_CERTDIR=/certs --volume jenkins-docker-certs:/certs/client --volume jenkins-data:/var/jenkins_home --publish 2376:2376 docker:dind
```
In the configuration of the pipeline the path should be CA5/Part2/Jenkinsfile.

### Step 3.1: Then, in the CA5 directory, create a Dockerfile with the following:
```dockerfile
FROM jenkins/jenkins:2.401.1-jdk17
USER root
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli
USER jenkins
RUN jenkins-plugin-cli --plugins "blueocean docker-workflow"
```

### Step 3.2: Create a docker-compose.yml file with the following:
```yaml
version: '3'

services:

  docker:
    container_name: docker
    image: docker:dind
    restart: always
    privileged: true
    networks:
      - jenkins
    environment:
      - DOCKER_TLS_CERTDIR=/certs
    volumes:
      - jenkins-docker-certs:/certs/client
      - jenkins-data:/var/jenkins_home
    ports:
      - "2376:2376"

  jenkins-blueocean:
    container_name: jenkins-blueocean
    build:
      context: .
      dockerfile: Dockerfile
    restart: on-failure
    networks:
      - jenkins
    environment:
      - DOCKER_HOST=tcp://docker:2376
      - DOCKER_CERT_PATH=/certs/client
      - DOCKER_TLS_VERIFY=1
    volumes:
      - jenkins-data:/var/jenkins_home
      - jenkins-docker-certs:/certs/client:ro
    ports:
      - "8080:8080"
      - "50000:50000"

networks:
  jenkins:
    driver: bridge

volumes:
  jenkins-data:
    external:
      name: jenkins-data  # Use existing volume named 'jenkins-data'
  jenkins-docker-certs:
    external:
      name: jenkins-docker-certs  # Use existing volume named 'jenkins-docker-certs'
```

Running the command `docker-compose up` will start the Jenkins and Docker containers.
Docker Compose is used in this setup as it simplifies the interconnection of the Docker containers required for Jenkins and Docker-in-Docker (docker:dind). 
The docker-compose.yml file defines two services: docker and jenkins-blueocean, each with specific configurations for networking, volumes, and environment variables. 
This approach ensures isolation between services while facilitating seamless communication via a custom bridge network (jenkins). 

To make sure the Javadoc is being generated, install the Javadoc and Publish HTML plugins in Jenkins. 
To ensure the pipeline can integrate Docker, install the Docker, Docker Pipeline, and CloudBees Docker Build and Publish plugins. 
Add your Docker Hub credentials to Jenkins. To do this, go to Manage Jenkins > Manage Credentials > Jenkins > Global credentials (unrestricted) > Add Credentials. 
Then, add your Docker Hub username and password.

The stages of the Jenkinsfile should be:
- Checkout: Checkout the code from the repository.
- Assemble: Compile and produce archive files with the application (without running tests).
- Test: Execute unit tests and publish the results using the junit step.
- Javadoc: Generate the javadoc of the project and publish it in Jenkins using the publishHTML step.
- Archive: Archive the generated files from the Assemble stage.
- Publish Image: Generate a Docker image with Tomcat and the WAR file and publish it to Docker Hub.

The docker images can be found here https://hub.docker.com/repository/docker/filipaba/devops/general.
### Step 3.3: Then, create a new pipeline in Jenkins with the following script:
```groovy
pipeline {
    agent any

    environment {
        CA_DIR = 'CA2/Part2/react-and-spring-data-rest-basic'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/fba-araujo/devops-23-24-PSM-1231826.git'
            }
        }
        stage('Assemble') {
            steps {
                dir(env.CA_DIR) {
                    sh 'chmod +x gradlew'
                    sh './gradlew assemble'
                }
            }
        }
        stage('Test') {
            steps {
                dir(env.CA_DIR) {
                    sh './gradlew test'
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
        stage('Javadoc') {
            steps {
                dir(env.CA_DIR) {
                    sh './gradlew javadoc'
                    publishHTML(target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: false,
                            keepAll: true,
                            reportDir: 'build/docs/javadoc',
                            reportFiles: 'index.html',
                            reportName: 'Javadoc'
                    ])
                }
            }
        }
        stage('Archive') {
            steps {
                dir(env.CA_DIR) {
                    archiveArtifacts artifacts: 'build/libs/*.war', fingerprint: true
                }
            }
        }
        stage('Create Dockerfile') {
            steps {
                echo 'Creating Dockerfile...'
                dir(env.CA_DIR) {
                    script {
                        def dockerfile = """
                        FROM tomcat:10-jdk17-openjdk-slim
                        RUN rm -rf /usr/local/tomcat/webapps/ROOT
                        COPY build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
                        EXPOSE 8080
                        CMD ["catalina.sh", "run"]
                        """
                        writeFile file: 'CA5', text: dockerfile
                    }
                }
            }
        }

        stage('Publish Image') {
            steps {
                script {
                    dir(env.CA_DIR) {
                        def dockerImage = docker.build("filipaba/devops:CA5", "-f CA5 .")
                        docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                            dockerImage.push()
                        }
                    }
                }
            }
        }
    }
}
```

In the Jenkins dashboard, you can click to build the pipeline. 
And in the Console Output tab, you can see the pipeline steps and, by scrolling to the end, confirm that the build was successful.

## Step 4: Tag Repository
- At the end of Part 2, mark your repository with the tag `ca5`.

## Conclusion
Working with Jenkins has been a great way to automate our software delivery processes. 
Jenkins helps us automate tasks like compiling code, running tests, and deploying applications. 
I found it to be really flexible, especially with its Jenkinsfile where we can define our workflows exactly how we need them. 
Although it took a bit of learning, Jenkins' plugins and community support have been essential, especially for integrating with tools like Docker for building and deploying containers.
Using Docker Compose was a big help in setting up our Jenkins environment. 
It made managing services like Jenkins and Docker-in-Docker (DinD) much easier. 
