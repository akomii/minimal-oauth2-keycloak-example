package org.example.oauth2.server.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyErrorController implements ErrorController {
  
  @RequestMapping("/error")
  public ModelAndView handleError(HttpServletRequest request) {
    ModelAndView modelAndView = new ModelAndView("error"); // use the name of your template
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());
      modelAndView.addObject("statusCode", statusCode);
    }
    Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    if (message != null) {
      String errorMessage = message.toString();
      modelAndView.addObject("errorMessage", errorMessage);
    }
    return modelAndView;
  }
}
