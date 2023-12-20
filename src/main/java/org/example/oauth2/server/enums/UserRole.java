/*
 * MIT License
 *
 * Copyright (c) 2023 Alexander Kombeiz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.oauth2.server.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum representing user roles in the application.
 *
 * <p>This enum defines two user roles, namely ADMIN and USER, that are used for access control
 * within the application. Each role implements the GrantedAuthority interface, allowing them to be
 * used for authentication and authorization.
 *
 * <p>The Code inner class provides authority strings associated with each role, which can be used
 * in security configurations.
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
    
    private Code() {}
  }
}
