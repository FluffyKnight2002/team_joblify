package com.ace_inspiration.team_joblify.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController{
    
    @RequestMapping("/error")
    public Object showErrorPage(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
                Integer statusCode = Integer.valueOf(status.toString());

                if(statusCode.equals(HttpStatus.NOT_FOUND.value())){
                    return "error-404";
                } else if(statusCode.equals(HttpStatus.FORBIDDEN.value())){
                    return "error-403";
                } else if(statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR.value())){
                    return "error-500";
                }
        }
        return null;
    }
}
