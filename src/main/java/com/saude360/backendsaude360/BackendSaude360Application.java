package com.saude360.backendsaude360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BackendSaude360Application {

    public static void main(String[] args) {
        SpringApplication.run(BackendSaude360Application.class, args);
    }

}
