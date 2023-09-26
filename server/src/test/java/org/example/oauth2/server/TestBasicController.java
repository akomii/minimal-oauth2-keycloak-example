package org.example.oauth2.server;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestBasicController extends AbstractKeycloakContainer {
  
  @Autowired
  MockMvc mvc;
  
  @Test
  @Order(1)
  void testPublicEndpoint() throws Exception {
    this.mvc.perform(get("/public"))
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("Public Hello World!")));
  }
  
  @Test
  @Order(2)
  void testUnauthenticatedSecuredEndpoint() throws Exception {
    this.mvc.perform(get("/home"))
      .andExpect(status().is3xxRedirection());
  }
  
  @Test
  @Order(3)
  void testAuthenticatedSecuredEndpoint() throws Exception {
    this.mvc.perform(get("/home").with(getBearerTokenFor("john")))
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("Hello, John Doe!")));
  }
  
  private RequestPostProcessor getBearerTokenFor(String username) {
    Keycloak keycloak = getKeycloakInstance(username, username);
    String token = keycloak.tokenManager().getAccessTokenString();
    return new RequestPostProcessor() {
      @Override
      public @NotNull MockHttpServletRequest postProcessRequest(@NotNull MockHttpServletRequest request) {
        request.addHeader("Authorization", "Bearer " + token);
        return request;
      }
    };
  }
  
  private Keycloak getKeycloakInstance(String username, String password) {
    return Keycloak.getInstance(
      KEYCLOAK_URL,
      DEFAULT_REALM_NAME,
      username,
      password,
      DEFAULT_CLIENT_NAME);
  }
}
