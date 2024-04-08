### Create branch tut-basic-gradle
git branch tut-basic-gradle

### Start working in the branch tut-basic-gradle
git checkout tut-basic-gradle

### Working folder
cd CA2/Part2/react-and-spring-data-rest-basic

### Create gradle spring boot project
Use https://start.spring.io to start a new gradle spring boot project with the following dependencies: 
Rest Repositories; Thymeleaf; JPA; H2.

You can  check the available gradle tasks by executing:
./gradlew tasks

In Employee the imports had a failed import there is no more javax now its jakarta 

Add the following plugin to build.gradle
```gradle
id "org.siouan.frontend-jdk17" version "8.0.0"
```

Add also the following code in build.gradle to configure the previous plug-in:
```gradle
frontend {
    nodeVersion = "16.20.2"
    assembleScript = "run build"
    cleanScript = "run clean"
    checkScript = "run check"
}
```

Update the scripts section/object in package.json to configure the execution of
webpack:
```json
"scripts": {
"watch": "webpack --watch -d --output ./target/classes/static/built/bundle.js",
"packageManager": "npm@9.6.7",
"webpack": "webpack",
"build": "npm run webpack",
"check": "echo Checking frontend",
"clean": "echo Cleaning frontend",
"lint": "echo Linting frontend",
"test": "echo Testing frontend"
}
```

### Execute the build
./gradlew build

### Execute the program
./gradlew bootRun





