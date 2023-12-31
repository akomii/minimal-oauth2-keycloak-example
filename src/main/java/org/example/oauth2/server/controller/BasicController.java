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

package org.example.oauth2.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.example.oauth2.server.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling various endpoints in the Spring Boot application. This
 * class defines methods to handle public and secured endpoints, including those that require
 * specific user roles for access. It also provides a method for logging out users.
 *
 * <p>The controller includes endpoints for displaying public content, user-specific content,
 * premium content for administrators and users with both roles, and a logout endpoint to log users
 * out of the application.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 28-09-2023
 */
@RestController
public class BasicController {

  @GetMapping("/public")
  public String publicEndpoint() {
    return "Public Hello World!";
  }

  @GetMapping("/home")
  public String securedEndpoint(@AuthenticationPrincipal OAuth2User user) {
    return printUserName(user);
  }

  @Secured(UserRole.Code.ADMIN)
  @GetMapping("/premium/admin")
  public String premiumEndpoint(@AuthenticationPrincipal OAuth2User user) {
    return printUserName(user);
  }

  @Secured({UserRole.Code.USER, UserRole.Code.ADMIN})
  @GetMapping("/premium/both")
  public String premiumEndpoint2(@AuthenticationPrincipal OAuth2User user) {
    return printUserName(user);
  }

  @GetMapping("/logout")
  public void logout(HttpServletRequest request) throws Exception {
    request.logout();
  }

  private String printUserName(OAuth2User user) {
    Optional<String> fullname = Optional.ofNullable(user.getAttribute("name"));
    if (fullname.isPresent()) {
      return String.format("Hello, %s!", fullname.get());
    }
    return "Hello, Stranger!";
  }
}
