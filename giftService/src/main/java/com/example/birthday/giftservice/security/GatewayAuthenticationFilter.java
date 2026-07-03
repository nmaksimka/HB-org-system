package com.example.birthday.giftservice.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter {
    private final String secret;

    public GatewayAuthenticationFilter(@Value("${security.gateway-secret}") String secret) {
        this.secret = secret;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/actuator/health")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!secret.equals(request.getHeader("X-Gateway-Secret"))
                || request.getHeader("X-User-Id") == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Gateway authentication required");
            return;
        }
        chain.doFilter(request, response);
    }
}
