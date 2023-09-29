package org.example.oauth2.server;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.oauth2.server.controller.BasicController;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@code TestBasicController} class is responsible for conducting integration tests on the
 * endpoints provided by the {@link BasicController}. It utilizes Spring Boot's testing framework
 * and Testcontainers to ensure the proper behavior of the OAuth2-secured endpoints.
 *
 * <p>This class contains test methods that verify the functionality of public and secured
 * endpoints. It establishes a connection to a Keycloak instance, simulates user authentication, and
 * performs HTTP requests to the endpoints, asserting the expected responses.
 *
 * @see BasicController
 * @see org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
 * @see org.springframework.boot.test.context.SpringBootTest
 * @see org.springframework.test.web.servlet.MockMvc
 * @see org.testcontainers.junit.jupiter.Testcontainers
 */
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestBasicController extends AbstractKeycloakContainer {

  /**
   * The Spring MVC Test framework's MockMvc instance for performing HTTP requests and assertions.
   */
  @Autowired MockMvc mvc;

  @Test
  @Order(1)
  void testPublicEndpoint() throws Exception {
    mvc.perform(get("/public"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Public Hello World!")));
  }

  @Test
  @Order(2)
  void testSecuredEndpoint() throws Exception {
    mvc.perform(get("/home")).andExpect(status().is3xxRedirection());
  }
}
