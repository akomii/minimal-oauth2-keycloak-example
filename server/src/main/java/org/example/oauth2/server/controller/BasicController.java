package org.example.oauth2.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.oauth2.server.annotations.PermissionsAnd;
import org.example.oauth2.server.annotations.PermissionsOr;
import org.example.oauth2.server.enums.OrganizationGroup;
import org.example.oauth2.server.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
    
    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public Hello World!";
    }
    
    @GetMapping("/home")
    public String securedEndpoint() {
        return "The non-public home";
    }
    
    @PermissionsAnd({UserRole.Code.USER, OrganizationGroup.Code.ORGANIZATION1})
    @GetMapping("/protected/roleAndGroup")
    public String securedByRoleAndGroup(@AuthenticationPrincipal OAuth2User user) {
        return String.format("Hello to protected User %s!", user.getName());
    }
    
    @PermissionsOr({UserRole.Code.USER, OrganizationGroup.Code.ORGANIZATION1})
    @GetMapping("/protected/roleOrGroup")
    public String securedByRoleOrGroup(@AuthenticationPrincipal OAuth2User user) {
        return String.format("Hello to protected User %s!", user.getName());
    }
    
    @Secured(UserRole.Code.ADMIN)
    @GetMapping("/premium")
    public String premiumEndpoint(@AuthenticationPrincipal OAuth2User user) {
        return String.format("Hello to premium User %s!", user.getName());
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }
}
