package com.greglturnquist.payroll;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * A class used to configure the Spring application when deployed as a WAR file in a servlet container.
 * Extends SpringBootServletInitializer to take advantage of Spring Boot's servlet initializer support.
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Overrides the configure method to configure the application.
     * This method is called by the servlet container during application startup.
     * @param application The SpringApplicationBuilder used to configure the application.
     * @return The configured SpringApplicationBuilder instance.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // Configures the application sources to be used for deployment.
        return application.sources(ReactAndSpringDataRestApplication.class);
    }

}