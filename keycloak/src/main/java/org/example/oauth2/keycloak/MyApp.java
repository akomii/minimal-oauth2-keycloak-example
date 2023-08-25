package org.example.oauth2.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyApp {
    
    public static void main(String[] args) {
        SpringApplication.run(org.example.oauth2.server.MyApp.class, args);
    }
}
