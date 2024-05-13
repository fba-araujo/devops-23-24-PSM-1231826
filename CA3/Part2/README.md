# Class Assignment 3 - Part 2

## Introduction
In this tutorial, our goal is to use Vagrant to setup a virtual environment to execute the tutorial Spring
Boot Application, gradle ”basic” version (developed in CA2-Part2). The setup includes two virtual machines (VMs) for running
the application: the *web* VM that executes the web application inside Tomcat9, and the *db* VM, which executes the H2
database as a server process. The web application connects to this VM.

## Description of the Requirements Implementation

### Step 1: Initial Solution
- As an initial solution to setup two VMs for running the Spring Basic Tutorial application, the steps described in
  https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/ were followed.

- **Clone Repository**: Create a local folder on your computer and clone the contents of the folder available in the repository using the following command:
  ```bash
    git clone https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/
    cd vagrant-multi-spring-tut-demo/
  ```
- **Initialize and Start Vagrant**: Execute the following commands in your terminal to initialize and start the Vagrant setup:
  ```bash
  vagrant init
  vagrant up
  ```
  
- **Test the Setup**: After initializing and starting Vagrant, you can test the setup by accessing the following URLs in your web browser:
    1. **Spring Web Application**:
        * http://localhost:8080/basic-0.0.1-SNAPSHOT/
        * http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/
    2. **H2 Console**:
        * http://localhost:8080/basic-0.0.1-SNAPSHOT/h2-console
        * http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/h2-console
    3. Use the following connection string when prompted:
      `jdbc:h2:tcp://192.168.56.11:9092/./jpadb`

### Step 2: Study Vagrantfile
- **Examine the Vagrantfile**: Take a close look at the Vagrantfile to understand how it creates and provisions two virtual machines (VMs):
    - **web**: This VM is responsible for running Tomcat and the Spring Boot basic application.
    - **db**: This VM executes the H2 server database.

### Step 3: Copy the Vagrantfile to your repository
- - First, create a new folder for this assignment in your repository:
  ```bash
    mkdir devops-23-24-PSM-1231826/CA3/Part2
  ```
- Then, copy the Vagrantfile to your repository (inside the folder for this assignment):
  ```bash
    cp ~/vagrant-multi-spring-tut-demo/Vagrantfile ~/devops-23-24-PSM-1231826/CA3/Part2
  ```

### Step 4: Update the Vagrantfile configuration
- Ensure your repository is public before executing the Vagrantfile.
- Go to the Vagrantfile to change the configuration
  ```bash
    cd devops-23-24-PSM-1231826/CA3/Part2
    nano Vagrantfile
  ```

- Update the jdk version (This provision is common for both VMs)
  ```bash
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
         openjdk-17-jdk-headless
  ```  

- Change the Vagrantfile configuration so that it uses Tomcat10
  ```bash
  # We want to access tomcat from the host using port 8080
  web.vm.network "forwarded_port", guest: 8080, host: 8080

  web.vm.provision "shell", inline: <<-SHELL, privileged: true
  ```
    ```bash
      # Use set -e command to exit automatically if any shell command fails
      set -e

      # Install Tomcat10 (sudo apt install tomcat10 fails)
      wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.0.18/bin/apache-tomcat-10.0.18.tar.gz
      tar -xvf apache-tomcat-10.0.18.tar.gz
      sudo mv apache-tomcat-10.0.18 /opt/tomcat10
      sudo ln -s /opt/tomcat10 /usr/local/tomcat10
      sudo chown -R vagrant:vagrant /opt/tomcat10
    ```

- Change the Vagrantfile configuration so that it uses your own gradle version of the spring application
  ```bash
    # Change the following command to clone your own repository!
    git clone https://github.com/fba-araujo/devops-23-24-PSM-1231826.git
    cd devops-23-24-PSM-1231826/CA2/Part2/react-and-spring-data-rest-basic
    chmod u+x gradlew
    ./gradlew clean build
  
    # Instead of using Gradle bootRun, redirect error output to a log
    nohup ./gradlew bootRun > /home/vagrant/spring-boot-app.log 2>&1 &
  ```

  - To deploy the war file to tomcat10 and start it:
  ```bash
      # To deploy the war file to tomcat10 do the following command:
      sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat10/webapps

      # Startup Tomcat
      /opt/tomcat10/bin/startup.sh
  ```
- Before running the Vagrant file, I had to make changes to the *build.gradle* file in *CA2-Part2* namely:

  - Applied the war plugin, which adds support for building WAR (Web Archive) files.
  ```gradle
  plugins {
  id 'war'
  }
  ```

  - Declared dependencies for testing and for runtime. As well as excluded a specific module from being included in the test dependencies.
  ```gradle
  dependencies {
  testImplementation('org.springframework.boot:spring-boot-starter-test') {
  exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
  }
  // To support war file for deploying to tomcat
  providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
  }
  ```

  - Set up the build to print system architecture and OS name, and dynamically set the node distribution URL pattern based on the system architecture and OS. It also configures the script to run webpack during assembly.
  ```gradle
  frontend {
  nodeVersion = "16.20.2"

  System.out.println(String.format("I am running on: %s(%s)", System.getProperty("os.arch"), System.getProperty("os.name")))

  if (System.getProperty("os.arch").equals("aarch64")) {
  if (System.getProperty("os.name").equals("Linux")) {
  nodeDistributionUrlPathPattern = 'vVERSION/node-vVERSION-linux-arm64.TYPE'
  }

        if (System.getProperty("os.name").equals("Mac OS X")) {
            nodeDistributionUrlPathPattern.set("vVERSION/node-vVERSION-darwin-x64.TYPE")
        }
  }

  assembleScript = 'run webpack'
  //assembleScript = "run build"
  //cleanScript = "run clean"
  //checkScript = "run check"
  }
    ```

  - Specified that the cleanupWebpack task should be executed before the build task. It establishes a dependency relationship between the two tasks, ensuring that webpack-generated files are cleaned up before the build process starts.
  ```gradle
  task cleanupWebpack(type: Delete) {
  group = 'Cleanup'
  description = 'Delete all files generated by webpack'

  // Define the directory containing webpack-generated files
  def webpackOutputDir = file('src/main/resources/static/built')

  // Configure the cleanup operation
  delete webpackOutputDir
  }

  build.dependsOn cleanupWebpack
  ```
  
- Execute `sudo vagrant up`

- NOTE: If the command above does not work, it may be necessary to run `sudo vagrant destroy` before it. 

### Step 5: Test the Spring Web application and the H2 console
- Open the Spring Web application in your browser: http://localhost:8080/
- Open the H2 console in your browser: http://localhost:8082/

### Step 6: Mark Repository with Tag
- At the completion of Part 2 of this assignment, tag your repository with `ca3-part2`.

## Issues
- While progressing through the assignment, an issue arose during the execution of vagrant up 
in the final step of [Step 4: Update the VagrantFile configuration](#step-4-update-the-vagrantfile-configuration). This issue stemmed from a dependency on the Gradle clean build process, resulting in build failures that necessitated modifications.

## Conclusion
In summary, this assignment focused on using Vagrant to create a virtual environment for running a Spring Boot application
tutorial. By following provided instructions, including setup, configuration, and testing, the assignment effectively
demonstrated proficiency in virtualization techniques using Vagrant.