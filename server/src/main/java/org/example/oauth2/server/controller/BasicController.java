package org.example.oauth2.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.oauth2.server.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BasicController {
  
  @GetMapping("/public")
  public String publicEndpoint() {
    return "Public Hello World!";
  }
  
  @GetMapping("/home")
  public String securedEndpoint(@AuthenticationPrincipal OAuth2User user) {
    System.out.println(user.getAttributes());
    
    Optional<String> fullname = Optional.ofNullable(user.getAttribute("name"));
    if (fullname.isPresent()) {
      return String.format("Hello, %s!", fullname.get());
    }
    return "Hello, Stranger!";
  }
  
  @Secured(UserRole.Code.ADMIN)
  @GetMapping("/premium")
  public String premiumEndpoint(@AuthenticationPrincipal OAuth2User user) {
    return "Hello to premium User!";
  }
  
  @GetMapping("/logout")
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }
}
