package org.example.oauth2.server.conf;

import org.springframework.beans.factory.annotation.Value;
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
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    
    @Value("${spring.security.oauth2.client.provider.my-oauth2-client.logout-url}")
    private String logoutUrl;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/public").permitAll().anyRequest().authenticated());
        http.oauth2Login();
        http.logout(logout -> logout
                .logoutSuccessHandler((request, response, authentication) ->
                        response.sendRedirect(logoutUrl))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"));
        return http.build();
    }
    
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
            return mappedAuthorities;
        };
    }
    
    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> claim, String authorityType) {
        return claim.stream().map(role -> new SimpleGrantedAuthority(authorityType + role)).collect(Collectors.toList());
    }
}
