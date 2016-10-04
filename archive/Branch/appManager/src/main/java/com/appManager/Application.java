package com.appManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
/**
 * File : Application.java 
 * Description : Class which start the Spring
 * boot process for microservice application 
 * Revision History : 
 * Version 	Date  	Author Reason 
 * 0.1 	27-May-2016 559296 Initial version
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer{
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    public static void main(String[] args) {
    	  SpringApplication.run(Application.class, args);
    }
}
