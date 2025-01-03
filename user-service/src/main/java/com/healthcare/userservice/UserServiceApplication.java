package com.healthcare.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {
//		"com.healthcare.userservice", // Main package
//		"com.health_care.unique_id_generator" // Package for unique ID generator or other components
//})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
