package com.ace_inspiration.team_joblify.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController{
    
@GetMapping("/error")
    public Object showErrorPage(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode.equals(HttpStatus.NOT_FOUND.value())) {
                return "redirect:/404";
            } else if (statusCode.equals(HttpStatus.FORBIDDEN.value())) {
                return "redirect:/403";
            } else if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR.value())) {
                return "redirect:/500";
            } else if (statusCode.equals(HttpStatus.BAD_REQUEST.value())) {
                return "redirect:/400";
            } else if (statusCode.equals(HttpStatus.UNAUTHORIZED.value())) {
                return "redirect:/401";
            } else if (statusCode.equals(HttpStatus.BAD_GATEWAY.value())) {
                return "redirect:/502";
            } else if (statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE.value())) {
                return "redirect:/503";
            } else if (statusCode.equals(HttpStatus.GATEWAY_TIMEOUT.value())) {
                return "redirect:/504";
            } else if (statusCode.equals(HttpStatus.METHOD_NOT_ALLOWED.value())) {
                return "redirect:/405";
            }
        }

        return null;
    }
}
