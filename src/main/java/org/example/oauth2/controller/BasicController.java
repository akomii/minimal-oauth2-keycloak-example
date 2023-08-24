package org.example.oauth2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.oauth2.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO error page
// TODO login redirect
// TODO logout redirect
// TODO comms via https

// TODO check permission by username
// TODO check permission by organization

// TODO add/remove role to user by admin
// TODO add/remove org to user by admin
// TODO let admin to everything

// TODO get organization infos
// TODO get user attributes

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
    
    @Secured(UserRole.Code.USER)
    @GetMapping("/protected")
    public String endpoint3(@AuthenticationPrincipal OAuth2User user) {
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
