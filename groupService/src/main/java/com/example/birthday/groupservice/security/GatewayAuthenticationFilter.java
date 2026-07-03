package com.example.birthday.groupservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter {
    private final String gatewaySecret;

    public GatewayAuthenticationFilter(
            @Value("${security.gateway-secret}") String gatewaySecret) {
        this.gatewaySecret = gatewaySecret;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/actuator/health")
                || request.getRequestURI().startsWith("/v3/api-docs")
                || request.getRequestURI().startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        if (!gatewaySecret.equals(request.getHeader("X-Gateway-Secret"))
                || request.getHeader("X-User-Id") == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Gateway authentication required");
            return;
        }
        chain.doFilter(request, response);
    }
}
