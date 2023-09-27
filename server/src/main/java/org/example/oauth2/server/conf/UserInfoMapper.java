package org.example.oauth2.server.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class UserInfoMapper {
  
  @Bean
  public GrantedAuthoritiesMapper userRolesMapper() {
    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      authorities.stream()
        .filter(OidcUserAuthority.class::isInstance)
        .map(OidcUserAuthority.class::cast)
        .map(OidcUserAuthority::getUserInfo)
        .filter(userInfo -> userInfo.hasClaim("resource_access"))
        .map(userInfo -> (Map<String, Object>) userInfo.getClaim("resource_access"))
        .filter(resourceAccess -> resourceAccess.containsKey("oauth2-demo-client"))
        .map(resourceAccess -> (Map<String, Object>) resourceAccess.get("oauth2-demo-client"))
        .filter(oauth2DemoClient -> oauth2DemoClient.containsKey("roles"))
        .map(oauth2DemoClient -> (Collection<String>) oauth2DemoClient.get("roles"))
        .forEach(roles -> mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles)));
      return mappedAuthorities;
    };
  }
  
  Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> claim) {
    return claim.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
  }
}
