package org.example.oauth2.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.oauth2.server", "org.example.oauth2.keycloak"})
public class MyApp {
    
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
