package org.example.oauth2.server.enums;

import org.springframework.security.core.GrantedAuthority;

public enum OrganizationGroup implements GrantedAuthority {
    
    ORGANIZATION1(Code.ORGANIZATION1);
    
    private final String authority;
    
    OrganizationGroup(String authority) {
        this.authority = authority;
    }
    
    @Override
    public String getAuthority() {
        return authority;
    }
    
    public static class Code {
        public static final String ORGANIZATION1 = "GROUP_ORGANIZATION1";
    }
}
