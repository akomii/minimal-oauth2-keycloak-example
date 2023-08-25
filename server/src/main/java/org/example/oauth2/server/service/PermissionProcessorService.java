package org.example.oauth2.server.service;

import org.example.oauth2.server.annotations.PermissionsAnd;
import org.example.oauth2.server.annotations.PermissionsOr;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class PermissionProcessorService {
    
    @PreAuthorize("@permissionProcessor.checkOr(#root.method, authentication)")
    public void processOr(Method method) {}
    
    @PreAuthorize("@permissionProcessor.checkAnd(#root.method, authentication)")
    public void processAnd(Method method) {}
    
    public boolean checkOr(Method method, Authentication authentication) {
        PermissionsOr annotation = AnnotationUtils.findAnnotation(method, PermissionsOr.class);
        if (annotation != null) {
            for (String role : annotation.value()) {
                if (authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean checkAnd(Method method, Authentication authentication) {
        PermissionsAnd annotation = AnnotationUtils.findAnnotation(method, PermissionsAnd.class);
        if (annotation != null) {
            for (String role : annotation.value()) {
                if (authentication.getAuthorities().stream()
                        .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
