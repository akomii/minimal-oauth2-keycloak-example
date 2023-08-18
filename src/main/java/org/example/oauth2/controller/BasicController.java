package org.example.oauth2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
    
    @GetMapping("/public")
    public String endpoint1() {
        return "Hello World!";
    }
    
    @GetMapping("/home")
    public String endpoint11() {
        return "My not public home";
    }
    
    @GetMapping("/claims")
    public String endpoint2(@AuthenticationPrincipal OidcUser user) {
        System.out.println(user.getAuthorities());
        return user.getClaims().toString();
    }
    
    @Secured("ROLE_USER")
    @GetMapping("/protected")
    public String endpoint33(@AuthenticationPrincipal OidcUser user) {
        return String.format("Hello, %s!", user.getClaimAsString("preferred_username"));
    }
    
    @Secured("ROLE_ADMIN")
    @GetMapping("/premium")
    public String endpoint3(@AuthenticationPrincipal OidcUser user) {
        return String.format("Hello, %s!", user.getClaimAsString("preferred_username"));
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }
}
