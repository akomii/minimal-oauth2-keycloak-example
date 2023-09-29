package org.example.oauth2.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Spring Boot application.
 *
 * <p>This class serves as the entry point for the application and is responsible for bootstrapping
 * the Spring Boot application using the SpringApplication.run() method. It initializes and starts
 * the application with the specified configuration.
 *
 * @see org.springframework.boot.SpringApplication
 * @since 28-09-2023
 */
@SpringBootApplication
public class MyApp {

  public static void main(String[] args) {
    SpringApplication.run(MyApp.class, args);
  }
}
