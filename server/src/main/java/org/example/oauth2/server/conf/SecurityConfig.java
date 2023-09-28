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
 * application. This class enables web security and method-level security, defines HTTP
 * security rules, and configures OAuth2 login and logout behavior. It also handles cross-origin
 * resource sharing (CORS) and CSRF protection.
 * <p></p>
 * The class specifies access control rules for different endpoints, such as allowing public
 * access to certain paths and requiring authentication for others. It also configures OAuth2
 * login and logout behavior, including redirecting after a successful logout.
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
   * Custom variable that holds the URL to which users are redirected after successfully logging
   * out of the application.
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
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/public").permitAll()
        .anyRequest().authenticated());
    http.oauth2Login();
    http.logout(logout -> logout
        .logoutSuccessHandler((request, response, authentication) ->
          response.sendRedirect(logoutUrl))
        .invalidateHttpSession(true)
        .clearAuthentication(true)
        .deleteCookies("JSESSIONID"));
    http.cors().and().csrf().disable();
    return http.build();
  }
}
