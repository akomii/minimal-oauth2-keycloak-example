/*
 * MIT License
 *
 * Copyright (c) 2023 Alexander Kombeiz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
class TestBasicController extends AbstractKeycloakContainer {

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
