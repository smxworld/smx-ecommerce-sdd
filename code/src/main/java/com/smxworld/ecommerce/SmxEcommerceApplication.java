package com.smxworld.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the application: a single Spring Boot process organized as a Spring Modulith, where each direct
 * sub-package of {@code com.smxworld.ecommerce} is an application module. See system/architecture.md.
 */
@SpringBootApplication
public class SmxEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmxEcommerceApplication.class, args);
    }
}
