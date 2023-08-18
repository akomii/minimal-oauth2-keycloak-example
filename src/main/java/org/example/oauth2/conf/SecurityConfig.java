package org.example.oauth2.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/public").permitAll()
                .anyRequest().authenticated();
        
        http.oauth2Login()
                .defaultSuccessUrl("/home")
                .userInfoEndpoint()
                .oidcUserService(oidcUserService());
        
        http.logout(logout -> logout
                .logoutSuccessHandler((request, response, authentication) -> {
                    String logoutUrl = "http://localhost:8180/auth/realms/oauth2-demo-realm/protocol/openid-connect/logout";
                    response.sendRedirect(logoutUrl);
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"));
        return http.build();
    }
    
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (userRequest) -> {
            OAuth2User oauth2User = delegate.loadUser(userRequest);
            OidcIdToken idToken = userRequest.getIdToken();
            Map<String, Object> claims = idToken.getClaims();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            
            if (claims.containsKey("realm_access")) {
                Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
                Collection<String> roles = (Collection<String>) realmAccess.get("roles");
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
            }
            
            // Add other authorities if needed
            authorities.addAll(oauth2User.getAuthorities());
            
            // Construct the OidcUserInfo
            OidcUserInfo userInfo = new OidcUserInfo(oauth2User.getAttributes());
            
            // Construct the OidcUser
            return new DefaultOidcUser(authorities, idToken, userInfo, "preferred_username");
        };
    }


    
    /*
    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
    
    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
*/

}
