package org.example.oauth2.server.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum representing user roles in the application.
 * <p></p>
 * This enum defines two user roles, namely ADMIN and USER, that are used for access
 * control within the application.
 * Each role implements the GrantedAuthority interface, allowing them to be used for
 * authentication and authorization.
 * <p></p>
 * The Code inner class provides authority strings associated with each role, which can
 * be used in security configurations.
 *
 * @see org.springframework.security.core.GrantedAuthority
 * @since 28-09-2023
 */
public enum UserRole implements GrantedAuthority {
  
  /**
   * Represents the ADMIN role with the authority string "ROLE_ADMIN".
   */
  ADMIN(Code.ADMIN),
  /**
   * Represents the USER role with the authority string "ROLE_USER".
   */
  USER(Code.USER);
  
  private final String authority;
  
  UserRole(String authority) {
    this.authority = authority;
  }
  
  @Override
  public String getAuthority() {
    return authority;
  }
  
  /**
   * Inner class containing authority strings for user roles.
   */
  public static class Code {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
  }
}
