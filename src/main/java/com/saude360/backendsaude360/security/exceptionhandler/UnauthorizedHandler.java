package com.saude360.backendsaude360.security.exceptionhandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class UnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException.getCause() instanceof JWTVerificationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido\", \"message\": \"O token informado é inválido. " + authException.getMessage() + "\"}");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Acesso negado\", \"message\": \"Você não tem permissão para acessar este recurso. " + authException.getMessage() + "\"}");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }
}
