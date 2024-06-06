# Use the Tomcat base image
FROM tomcat:10-jdk17-openjdk-slim

# Remove the default ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy the WAR file to the webapps directory in Tomcat
COPY build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

