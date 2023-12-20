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

package org.example.oauth2.server.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class responsible for configuring security settings for the Spring Boot
 * application. This class enables web security and method-level security, defines HTTP security
 * rules, and configures OAuth2 login and logout behavior. It also handles cross-origin resource
 * sharing (CORS) and CSRF protection.
 *
 * <p>The class specifies access control rules for different endpoints, such as allowing public
 * access to certain paths and requiring authentication for others. It also configures OAuth2 login
 * and logout behavior, including redirecting after a successful logout.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 28-09-2023
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  /**
   * Custom variable that holds the URL to which users are redirected after successfully logging out
   * of the application.
   */
  @Value("${spring.security.oauth2.client.provider.my-oauth2-client.logout-url}")
  private String logoutUrl;

  /**
   * Configures the security filter chain for handling HTTP security in the Spring Boot application.
   * This method defines the security rules for different endpoints, specifying which paths are
   * public and which require authentication. It also configures OAuth2 login and logout behavior,
   * including the redirect after a successful logout. Additionally, it disables cross-origin
   * resource sharing (CORS) and CSRF protection.
   *
   * @param http The HttpSecurity object used to configure security rules.
   * @return A SecurityFilterChain instance representing the configured security filter chain.
   * @throws Exception If an error occurs during configuration.
   * @since 28-09-2023
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
    http.authorizeHttpRequests(
        authorize ->
            authorize
                .requestMatchers("/public", "/error", "/login")
                .permitAll()
                .anyRequest()
                .authenticated());
    http.oauth2Login();
    http.logout(
        logout ->
            logout
                .logoutSuccessHandler(
                    (request, response, authentication) -> response.sendRedirect(logoutUrl))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"));
    return http.build();
  }
}
