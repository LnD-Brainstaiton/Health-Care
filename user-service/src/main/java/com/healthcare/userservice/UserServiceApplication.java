package com.healthcare.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
/*@ComponentScan(basePackages = {
		"com.health_care.user_service", // Main package
		"com.health_care.unique_id_generator" // Package for unique ID generator or other components
})*/
@EnableFeignClients
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
