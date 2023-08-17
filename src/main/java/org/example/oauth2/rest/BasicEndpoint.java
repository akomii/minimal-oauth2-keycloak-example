package org.example.oauth2.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicEndpoint {
    
    @GetMapping("/public")
    public String endpoint1() {
        return "Hello World!";
    }
    
    @GetMapping("/protected")
    public String endpont2(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Hello, %s!", jwt.getClaimAsString("preferred_username"));
    }
    
    @GetMapping("/premium")
    public String endpoint3(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Hello, %s!", jwt.getClaimAsString("preferred_username"));
    }
}
