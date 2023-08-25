package com.ace_inspiration.team_joblify.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

                if (!(exception instanceof DisabledException)) {
                    // Handle incorrect credentials condition
                    response.sendRedirect("/login?error=true");
                } else {
                    // Handle disabled account condition
                    response.sendRedirect("/login?disabled=true");
                }
}
}