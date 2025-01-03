package com.healthcare.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/application.properties")
public class ConstantFieldConfiguration {
}