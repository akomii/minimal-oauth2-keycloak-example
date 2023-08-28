package org.example.oauth2.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.oauth2.server.annotations.PermissionsAnd;
import org.example.oauth2.server.annotations.PermissionsOr;
import org.example.oauth2.server.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
    
    @GetMapping("/public")
    public String endpoint1() {
        return "Public Hello World!";
    }
    
    @GetMapping("/home")
    public String endpoint2() {
        return "My not public home";
    }
    
    @PermissionsAnd({UserRole.Code.USER, "GROUP_ORGANIZATION2"})
    @GetMapping("/protected")
    public String roleAndGroup(@AuthenticationPrincipal OAuth2User user) {
        return String.format("Hello to protected User %s!", user.getName());
    }
    
    @PermissionsOr({UserRole.Code.USER, "GROUP_ORGANIZATION2"})
    @GetMapping("/protected2")
    public String roleOrGroup(@AuthenticationPrincipal OAuth2User user) {
        return String.format("Hello to protected User %s!", user.getName());
    }
    
    @Secured(UserRole.Code.ADMIN)
    @GetMapping("/premium")
    public String endpoint4(@AuthenticationPrincipal OAuth2User user) {
        return String.format("Premium to premium User %s!", user.getName());
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }
}
