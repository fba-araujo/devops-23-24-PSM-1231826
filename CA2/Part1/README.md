# Class Assigment 2

## Introduction
In this tutorial, we'll be working with an existing application that implements a basic multithreaded chat room server. Adding new features to it.

## Table of Contents
- [Part 1](#Part-1)
- [Step 1: Downloading and Committing the example application](#step-1-downloading-and-committing-the-example-application-)
- [Step 2: Execute the server](#step-2-execute-the-server)


## Part 1
The goal is to practice using graddle.

### Step 1: Downloading and Committing the example application

    % git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git

### Step 2: Execute the server
Changed my Gradle distribution to version 8.5

Updated gradle-wrapper.properties
``` properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
```

cd ca2/part1/gradle_basic_demo

Build
-----
    % ./gradlew build 

Run the server
--------------
    % java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp 59001

Run two clients
------------
    % ./gradlew runClient
