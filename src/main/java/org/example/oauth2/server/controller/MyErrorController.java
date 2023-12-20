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

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class responsible for handling custom error pages in the Spring Boot application. This
 * class implements the ErrorController interface to handle error requests and display custom error
 * pages with status code and error message information.
 *
 * <p>It defines a method to handle error requests, extract error details, and render a custom error
 * page to provide a user-friendly error experience.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 28-09-2023
 */
@Controller
public class MyErrorController implements ErrorController {

  private static final String ERROR_TEMPLATE = "error"; // use the name of your template html
  private static final String STATUS_CODE_ATTRIBUTE = "statusCode";
  private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";

  /**
   * Handles error requests and renders a custom error page with status code and error message
   * information.
   *
   * <p>This method is responsible for processing error requests and rendering a user-friendly error
   * page that includes the HTTP status code and error message, if available. It extracts these
   * details from the request attributes and populates them in the ModelAndView object for display.
   *
   * @param request The HttpServletRequest containing error details.
   * @return A ModelAndView representing the custom error page with status code and error message.
   * @since 28-09-2023
   */
  @RequestMapping("/error")
  public ModelAndView handleError(HttpServletRequest request) {
    ModelAndView modelAndView = new ModelAndView(ERROR_TEMPLATE);
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      modelAndView.addObject(STATUS_CODE_ATTRIBUTE, Integer.valueOf(status.toString()));
    }
    Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    if (message != null) {
      modelAndView.addObject(ERROR_MESSAGE_ATTRIBUTE, message.toString());
    }
    return modelAndView;
  }
}
