package org.example.oauth2.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.example.oauth2.server.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling various endpoints in the Spring Boot application.
 * This class defines methods to handle public and secured endpoints, including those that require
 * specific user roles for access. It also provides a method for logging out users.
 * <p></p>
 * The controller includes endpoints for displaying public content, user-specific content,
 * premium content for administrators and users with both roles, and a logout endpoint to
 * log users out of the application.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 28-09-2023
 */
@RestController
public class BasicController {
  
  @GetMapping("/public")
  public String publicEndpoint() {
    return "Public Hello World!";
  }
  
  @GetMapping("/home")
  public String securedEndpoint(@AuthenticationPrincipal OAuth2User user) {
    return printUserName(user);
  }
  
  @Secured(UserRole.Code.ADMIN)
  @GetMapping("/premium/admin")
  public String premiumEndpoint(@AuthenticationPrincipal OAuth2User user) {
    return printUserName(user);
  }
  
  @Secured({UserRole.Code.USER, UserRole.Code.ADMIN})
  @GetMapping("/premium/both")
  public String premiumEndpoint2(@AuthenticationPrincipal OAuth2User user) {
    return printUserName(user);
  }
  
  @GetMapping("/logout")
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }
  
  private String printUserName(OAuth2User user) {
    Optional<String> fullname = Optional.ofNullable(user.getAttribute("name"));
    if (fullname.isPresent()) {
      return String.format("Hello, %s!", fullname.get());
    }
    return "Hello, Stranger!";
  }
}
