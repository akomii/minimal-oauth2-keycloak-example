package org.example.oauth2.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/public").permitAll().anyRequest().authenticated());
        
        http.oauth2Login();
        
        http.logout(logout -> logout.logoutSuccessHandler((request, response, authentication) -> {
            String logoutUrl = "http://localhost:8180/realms/oauth2-demo-realm/protocol/openid-connect/logout";
            response.sendRedirect(logoutUrl);
        }).invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID"));
        return http.build();
    }
    
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;
            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getUserInfo();
                if (userInfo.hasClaim("roles")) {
                    var roles = (Collection<String>) userInfo.getClaim("roles");
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles, "ROLES_"));
                }
            }
            return mappedAuthorities;
        };
    }
    
    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> claim, String authorityType) {
        return claim.stream().map(role -> new SimpleGrantedAuthority(authorityType + role)).collect(Collectors.toList());
    }
}
