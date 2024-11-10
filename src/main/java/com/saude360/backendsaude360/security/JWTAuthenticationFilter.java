package com.saude360.backendsaude360.security;

import com.saude360.backendsaude360.exceptions.TokenInvalidException;
import com.saude360.backendsaude360.services.UserDetailsSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTToken jwtToken;
    private final UserDetailsSecurityService userDetailsSecurityService;

    public JWTAuthenticationFilter(JWTToken jwtToken, UserDetailsSecurityService userDetailsSecurityService) {
        this.jwtToken = jwtToken;
        this.userDetailsSecurityService = userDetailsSecurityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try  {
            var token = tokenRecovery(request);

            if (token != null) {
                var login = jwtToken.validateToken(token);
                var user = userDetailsSecurityService.loadUserByUsername(login);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (TokenInvalidException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido\", \"message\": \"" + e.getMessage() + "\"}");
        }

    }

    private String tokenRecovery(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
