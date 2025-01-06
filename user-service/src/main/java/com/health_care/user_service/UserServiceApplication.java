package com.health_care.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.health_care.user_service", // Main package
		"com.health_care.unique_id_generator" // Package for unique ID generator or other components
})
@EnableFeignClients(basePackages = "com.health_care.user_service.feign")
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
