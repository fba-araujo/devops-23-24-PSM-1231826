# Class Assignment 3 - Part 1

## Introduction
In this tutorial, our goal is to walk you through the process of setting up a Virtual Machine (VM) using VirtualBox and installing Ubuntu on it. We'll start by guiding you through the VM setup process, including configuring network settings and installing essential packages. Additionally, we'll demonstrate how to set up SSH and FTP servers, enabling remote access to the VM. Furthermore, we'll cover tasks such as cloning a repository inside the VM, building and executing Spring Boot and Gradle projects, and troubleshooting any potential issues that may arise.

## Table of Contents
- [Step 1: Before Beginning the Class Assignment 3](#step-1-before-beginning-the-class-assignment-3)
- [Step 2: Clone the repository inside the VM.](#step-2-clone-the-repository-inside-the-vm)
- [Step 3: Build and Execute Projects](#step-3-build-and-execute-projects)
    - [Step 3.1: Spring Boot tutorial basic project](#step-31-spring-boot-tutorial-basic-project)
        - [Step 3.1.1: Spring Boot with Maven](#step-311-spring-boot-with-maven)
        - [Step 3.1.2: Spring Boot with Gradle](#step-312-spring-boot-with-gradle)
        - [Step 3.1.3: Open project frontEnd on browser:](#step-313-open-project-frontend-on-browser)
    - [Step 3.2: Gradle Basic Demo project](#step-32-gradle-basic-demo-project)
- [Step 4: Commit Tag](#step-4-commit-tag)
- [Issues and Troubleshooting](#issues-and-troubleshooting)
- [Conclusion](#conclusion)


## Step 1: Before Beginning the Class Assignment 3

1. **Create VM**
    - Create a new VM in VirtualBox.

2. **Change VM Settings**
    - Connect the Ubuntu 18.04 minimal installation ISO to the VM.
    - Set VM RAM to 2048 MB.
    - Set Network Adapter 1 as NAT.
    - Set Network Adapter 2 as Host-only Adapter (vboxnet0).

3. **Start VM and Install Ubuntu**
    - Start the VM and install Ubuntu using the minimal installation media.

4. **Configure Host-only Network**
    - From the main menu in VirtualBox, go to `File -> Host Network Manager (Ctrl+W)`.
    - Create a new Host-only network.
    - Select a name for your host-only network in the VM's network configuration.

5. **Set IP Address**
    - Check the IP address range of the Host-only network (e.g., 192.168.56.1/24).
    - Configure the second adapter of the VM with an IP address within this range.
    - This static IP configuration is crucial for establishing a consistent network connection between the host and guest machines, enabling seamless communication for SSH, file transfers, and other network-based operations.

6. **Update Packages and Install Network Tools**
   ```bash
   sudo apt update
   sudo apt install net-tools
   ```

7. **Edit Network Configuration**
   ```bash
   sudo nano /etc/netplan/01-netcfg.yaml
   ```

   ```yaml
   network:
      version: 2
      renderer: networkd
      ethernets:
         enp0s3:
            dhcp4: yes
         enp0s8:
            addresses:
               - 192.168.56.5/24
   ```

8. **Ensure the configuration file is similar to the provided example.**
   ```bash
   sudo netplan apply
   ```
   
9. **Install SSH Server**
   ```bash
   sudo apt install openssh-server
   ```

10. **Enable password authentication for ssh**
- Uncomment the line PasswordAuthentication yes
  ```bash
  sudo nano /etc/ssh/sshd_config
  sudo service ssh restart
  ```

11. **Install FTP Server**
     ```bash
     sudo apt install vsftpd
     ```
    
12. **Edit vsftpd configuration to enable write access.**
- Uncomment the line write_enable=YES
  ```bash
   sudo nano /etc/vsftpd.conf
   sudo service vsftpd restart
  ```

13. **Connect to VM via SSH**
- Use SSH to connect to the VM from the host machine:
   ```bash
   ssh fba@192.168.56.5
   ```
14. **Install Git and Java**
      ```bash
       sudo apt install git
       sudo apt install openjdk-8-jdk-headless
      ```

15. **Clone and Run Spring Tutorial Application**
- Clone the Spring Tutorial application:
   ```bash
    git clone https://github.com/spring-guides/tut-react-and-spring-data-rest.git
   ```
- Navigate to the basic version directory:
      ```bash
       cd tut-react-and-spring-data-rest/basic
      ```
- Build and run the application:
      ```bash
       ./mvnw spring-boot:run
      ```
- Access the application by opening a web browser on your host machine and navigating to the specified URL, typically http://<VM_IP>:8080/. 
- (e.g., http://192.168.56.5:8080/).

16. **Establish a connection between the host and the guest (VM)**
   ```bash
   ssh <name>@<IP>
   ```
(e.g., ssh fba@192.168.56.5).

## Step 2: Clone the repository inside the VM.
- Repository must be public
```bash
git clone https://github.com/fba-araujo/devops-23-24-PSM-1231826.git
```

## Step 3: Build and Execute Projects
### Step 3.1.: Spring Boot tutorial basic project
### Step 3.1.1: Spring Boot with Maven
```bash
cd devops-23-24-PSM-1231826/CA1/tut-react-and-spring-data-rest/basic
```

#### Maven wrapper must be given permission to execute.
```bash
chmod +x mvnw
```

#### Try to build and execute the spring boot tutorial basic project
```bash
./mvnw compile
./mvnw spring-boot:run
```

### Step 3.1.2: Spring Boot with Gradle
```bash
cd devops-23-24-PSM-1231826/CA2/Part2/react-and-spring-data-rest-basic
```

#### Gradle wrapper must be given permission to execute.
```bash
chmod +x gradlew
```

#### Try to build and execute the spring boot tutorial basic project
```bash
./gradlew build
./gradlew bootRun
```

### Step 3.1.3: Open project frontEnd on browser:
Get the VM IP:
```bash
ip addr
```
Put the IP and the port 8080 on the browser address.
(e.g.,  192.168.56.5:8080).

### Step 3.2.: Gradle Basic Demo project
```bash
cd devops-23-24-PSM-1231826/CA2/Part1/gradle_basic_demo
```

#### Gradle wrapper and must be given permission to execute.
```bash
chmod +x gradlew
```

#### Build and Run the Gradle Basic project

```bash
./gradlew build
./gradlew runServer
```

#### To run the client in the same folder but on the computer terminal, on two different terminals:
```bash
./gradlew runClient --args="192.168.56.5 59001" 
```

## Step 4: Commit Tag
At the end of Part 1, mark your repository with the tag `ca3-part1`.

## Issues and Troubleshooting
- **Permission Errors:**
  When executing the Maven and Gradle Wrapper scripts, you may encounter errors related to insufficient permissions. To resolve this issue, grant execution permission to the wrapper scripts using the following commands:
  ```bash
  chmod +x mvnw
  chmod +x gradlew
    ```
  
## Conclusion
This assignment provided hands-on experience in setting up and utilizing a Virtual Machine (VM) with Ubuntu Linux, employing hypervisors like VirtualBox. Through key steps such as VM creation, configuration, SSH setup, and dependency installation, we gained practical insights into VM deployment, Linux administration, and Java project management.
By successfully building and running Java projects using Maven and Gradle within the Linux environment, we demonstrated proficiency in software development practices in a virtualized environment.