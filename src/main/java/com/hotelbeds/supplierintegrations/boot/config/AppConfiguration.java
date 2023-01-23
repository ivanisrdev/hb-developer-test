package com.hotelbeds.supplierintegrations.boot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.hotelbeds.supplierintegrations.hackertest")
@PropertySource("classpath:application.yml")
public class AppConfiguration {
}
