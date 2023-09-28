package org.example.oauth2.server.conf;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class OAuth2UserInfoMapper {
  
  @Value("${spring.security.oauth2.client.provider.my-oauth2-client.claims.roles}")
  private String rolesKeyChain;
  
  private String[] rolesKeys;
  
  @PostConstruct
  public void init() {
    rolesKeys = rolesKeyChain.split("\\.");
  }
  
  @Bean
  public GrantedAuthoritiesMapper myAuthoritiesMapper() {
    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      authorities.stream()
        .filter(OidcUserAuthority.class::isInstance)
        .map(OidcUserAuthority.class::cast)
        .map(OidcUserAuthority::getUserInfo)
        .filter(userInfo -> userInfo.hasClaim(rolesKeys[0]))
        .map(userInfo -> getNestedListForKey(userInfo.getClaims(), rolesKeys))
        .map(roles -> (Collection<String>) roles)
        .forEach(roles -> mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles)));
      return mappedAuthorities;
    };
  }
  
  private List<String> getNestedListForKey(Map<String, Object> map, String[] keys) {
    Object value = map;
    if (keys.length == 0) {
      throw new IllegalArgumentException("The keys array cannot be empty.");
    }
    for (String key : keys) {
      if (value instanceof Map) {
        value = ((Map<?, ?>) value).get(key);
        if (value == null) {
          throw new IllegalArgumentException(String.format("Key '%s' not found in the map.", key));
        }
      } else {
        throw new IllegalArgumentException(String.format("Key '%s' annot be accessed because it's not a map.", key));
      }
    }
    if (value instanceof List) {
      return (List<String>) value;
    } else {
      throw new IllegalArgumentException("Last key should contain a list or array of strings, but it does not.");
    }
  }
  
  private Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> claim) {
    return claim.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
  }
}
