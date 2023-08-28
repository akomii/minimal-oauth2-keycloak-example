package org.example.oauth2.keycloak.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class KeycloakSecurityConfig {
    
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.stream()
                    .filter(OidcUserAuthority.class::isInstance)
                    .map(OidcUserAuthority.class::cast)
                    .map(OidcUserAuthority::getUserInfo)
                    .filter(userInfo -> userInfo.hasClaim("roles"))
                    .map(userInfo -> (Collection<String>) userInfo.getClaim("roles"))
                    .forEach(roles -> mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles, "ROLE_")));
            authorities.stream()
                    .filter(OidcUserAuthority.class::isInstance)
                    .map(OidcUserAuthority.class::cast)
                    .map(OidcUserAuthority::getUserInfo)
                    .filter(groupInfo -> groupInfo.hasClaim("groups"))
                    .map(groupInfo -> (Collection<String>) groupInfo.getClaim("groups"))
                    .forEach(groups -> mappedAuthorities.addAll(generateAuthoritiesFromClaim(groups, "GROUP_")));
            return mappedAuthorities;
        };
    }
    
    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> claim, String authorityType) {
        return claim.stream().map(role -> new SimpleGrantedAuthority(authorityType + role)).collect(Collectors.toList());
    }
}
