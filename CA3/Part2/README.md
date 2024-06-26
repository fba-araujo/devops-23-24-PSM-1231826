# Class Assignment 3 - Part 2

## Introduction
In this tutorial, our goal is to use Vagrant to setup a virtual environment to execute the tutorial Spring
Boot Application, gradle ”basic” version (developed in CA2-Part2). The setup includes two virtual machines (VMs) for running
the application: the *web* VM that executes the web application inside Tomcat9, and the *db* VM, which executes the H2
database as a server process. The web application connects to this VM.

## Table of Contents
- [Introduction](#introduction)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
  - [Step 1: Initial Solution](#step-1-initial-solution)
  - [Step 2: Study Vagrantfile](#step-2-study-vagrantfile)
  - [Step 3: Copy the Vagrantfile to your repository](#step-3-copy-the-vagrantfile-to-your-repository)
  - [Step 4: Update the Vagrantfile configuration](#step-4-update-the-vagrantfile-configuration)
  - [Step 5: Update the React-and-spring-data-rest-basic gradle project](#step-5-update-the-react-and-spring-data-rest-basic-gradle-project)
  - [Step 6: Test the Spring Web application and the H2 console](#step-6-test-the-spring-web-application-and-the-h2-console)
  - [Step 7: Mark Repository with Tag](#step-7-mark-repository-with-tag)
- [Issues](#issues)
- [Alternative VMware Workspace](#alternative-vmware-workstation)
- [Conclusion](#conclusion)

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
      sudo cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /opt/tomcat10/webapps

      # Startup Tomcat
      /opt/tomcat10/bin/startup.sh
  ```

### Step 5: Update the React-and-spring-data-rest-basic gradle project

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

- Update app.js
  - Change method to fetch data when component mounts with full path
  ```javascript
    // Lifecycle method to fetch data when component mounts
    componentDidMount() { // <2>
    client({method: 'GET', path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
    this.setState({employees: response.entity._embedded.employees});
    });
    }
  ```
- Update application.properties to enable H2 database
  ```properties
  server.servlet.context-path=/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
  #To enable the H2 database so our Web VM in CA3/Part2 can communicate with the database in the DB VM in CA3/Part1

  #The command at the end prevents the database from closing when the last connection is closed
  spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
  spring.datasource.driverClassName=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=
  spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

  spring.jpa.hibernate.ddl-auto=update
  spring.h2.console.enabled=true
  spring.h2.console.path=/h2-console
  spring.h2.console.settings.web-allow-others=true
  ```

- Add ServletInitializer a class used to configure the Spring application when deployed as a WAR file in a servlet container.
  
- ```java
  public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    // Configures the application sources to be used for deployment.
    return application.sources(ReactAndSpringDataRestApplication.class);
    }
  }
  ```

- Execute `sudo vagrant up`

- NOTE: If the command above does not work, it may be necessary to run `vagrant destroy` before it. 

### Step 6: Test the Spring Web application and the H2 console
- Open the Spring Web application in your browser: http://192.168.56.10:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/ ([WEB VM IP]:[TOMCAT PORT]/[WAR FILE])
- Open the H2 console in your browser: http://localhost:8082/

### Step 7: Mark Repository with Tag
- At the completion of Part 2 of this assignment, tag your repository with `ca3-part2`.

## Issues
- While progressing through the assignment, an issue arose during the execution of vagrant up.
- [Step 5: Update the React-and-spring-data-rest-basic gradle project](#step-5-update-the-react-and-spring-data-rest-basic-gradle-project) consists in the modifications needed to make the project configure correctly.
- Adapting configurations to support Tomcat 10 within the Vagrant environment posed several challenges. 
- Migrating to Tomcat 10 introduced compatibility issues with existing components, requiring careful adjustments to ensure seamless integration. 
- Finding up-to-date documentation and resources for configuring Tomcat 10 within the Vagrant environment proved to be difficult. 
- Additionally, integrating the Gradle build process with the updated environment encountered issues related to dependency resolution, task execution, and build configuration.

## Alternative VMware Workstation
In this alternative approach, we'll use VMware to manage virtual machines for hosting both the Spring Boot application and the H2 database. 
VMware offers an intuitive interface and robust functionalities for virtualization, making it a suitable alternative to VirtualBox.

### Step 1: Configuring VMware Virtual Machines
Start by downloading and installing VMware Workstation on your local system.

### Step 2: Creating the Vagrantfile for Your Project Repository
- **Change the Box**: You need to use a Vagrant box that is compatible with VMware. Replace "ubuntu/bionic64" with a box that supports VMware, such as "bento/ubuntu-18.04".
  ```
  config.vm.box = "bento/ubuntu-18.04"
  db.vm.box = "bento/ubuntu-18.04"
  web.vm.box = "bento/ubuntu-18.04"
  ```

- **Specify VMware Provider**: Instead of specifying configurations specific to VirtualBox, you need to define configurations specific to the VMware provider.
  - For the webserver VM (web), I replaced the VirtualBox provider with the VMware provider (vmware_fusion) and specified the RAM memory setting for VMware.
  ```
  web.vm.provider "vmware_fusion" do |v|
      v.memory = 1024
  end
  ```

### Step 3: Operating Virtual Machines
**Accessing Applications**: To access the Spring Boot application, simply enter its IP address or hostname into your web browser. 
Likewise, access the H2 database console using its designated IP address and configured port.

### Step 4:  VMware Commands
Execute the command to launch the Vagrantfile using VMware. 
In case you make changes to your Vagrantfile and need to rerun it, ensure to delete the previously created VMs.
```bash
vagrant up --provider=vmware_desktop
```

- **Terminating VMs**: When finished, use the command to remove the VMs.
```bash
vagrant destroy -f  
```

## Conclusion
In summary, this assignment focused on using Vagrant to create a virtual environment for running a Spring Boot application
tutorial. By following provided instructions, including setup, configuration, and testing, the assignment effectively
demonstrated proficiency in virtualization techniques using Vagrant.