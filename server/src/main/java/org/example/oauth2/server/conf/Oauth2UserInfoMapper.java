package org.example.oauth2.server.conf;

import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

/**
 * This class is responsible for mapping user roles from OAuth2 claims to
 * Spring Security GrantedAuthorities. It extracts role information from OAuth2
 * user claims and transforms them into authorities that can be used for access
 * control within the application. The mapping is based on the configuration of
 * claim keys and follows the OAuth2 specification.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 28-09-2023
 */
@Configuration
public class Oauth2UserInfoMapper {
  
  /**
   * This variable holds a Spring configuration property that defines the dot-separated chain
   * of keys used to navigate and extract user roles from OAuth2 claims. It is used to configure
   * the mapping of roles from user claims to Spring Security authorities.
   */
  @Value("${spring.security.oauth2.client.provider.my-oauth2-client.claims.roles}")
  private String rolesKeyChain;
  
  private String[] rolesKeys;
  
  @PostConstruct
  public void init() {
    rolesKeys = rolesKeyChain.split("\\.");
  }
  
  /**
   * Generates a custom Spring Security GrantedAuthoritiesMapper to map user
   * roles from OAuth2 claims. This method defines the logic for extracting
   * and mapping roles from user claims to GrantedAuthorities. It iterates
   * through the claims, extracts role information, and converts them into
   * Spring Security authorities with "ROLE_" prefix, which can be used for access control.
   *
   * @return A GrantedAuthoritiesMapper for mapping user roles from OAuth2 claims to
   *     GrantedAuthorities.
   * @since 28-09-2023
   */
  @Bean
  public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      authorities.stream()
        .filter(OidcUserAuthority.class::isInstance)
        .map(OidcUserAuthority.class::cast)
        .map(OidcUserAuthority::getUserInfo)
        .map(userInfo -> getNestedListForKey(userInfo.getClaims(), rolesKeys))
        .map(roles -> (Collection<String>) roles)
          .forEach(roles -> mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles)));
      return mappedAuthorities;
    };
  }
  
  // Retrieves a nested list of values from a hierarchical map using a sequence of keys.
  private List<String> getNestedListForKey(Map<String, Object> map, String[] keys) {
    Object value = map;
    // Ensure that the keys array is not empty.
    if (keys.length == 0) {
      throw new IllegalArgumentException("The keys array cannot be empty.");
    }
    // Traverse the map hierarchy using the keys.
    for (String key : keys) {
      if (value instanceof Map) {
        value = ((Map<?, ?>) value).get(key);
        if (value == null) {
          throw new IllegalArgumentException(String.format("Key '%s' not found in the map.", key));
        }
      } else {
        throw new IllegalArgumentException(
          String.format("Key '%s' annot be accessed because it's not a map.", key)
        );
      }
    }
    // Check if the final value is a list and return it.
    if (value instanceof List) {
      return (List<String>) value;
    } else {
      throw new IllegalArgumentException(
        "Last key should contain a list or array of strings, but it does not.");
    }
  }
  
  private Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> claim) {
    return claim.stream().map(role ->
      new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
  }
}
