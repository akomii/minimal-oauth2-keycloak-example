package org.example.oauth2.keycloak.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String REALM_NAME = "oauth2-demo-realm";
    
    @Autowired
    private Keycloak keycloak;
    
    // /search-user?username=john&firstName=John
    @GetMapping("/search/user")
    public List<String> searchUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName) {
        logger.info("Searching with criteria - username: {}, email: {}, firstName: {}", username, email, firstName);
        
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
                .users()
                .search(username, email, firstName, null, 0, 100);
        
        return users.stream()
                .map(UserRepresentation::getUsername)
                .collect(Collectors.toList());
    }
}
