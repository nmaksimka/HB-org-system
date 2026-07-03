package com.example.birthday.mockbankservice.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class RequestAuthenticationFilter extends OncePerRequestFilter {
    private final String gatewaySecret;
    private final String internalApiKey;

    public RequestAuthenticationFilter(
            @Value("${security.gateway-secret}") String gatewaySecret,
            @Value("${security.internal-api-key}") String internalApiKey) {
        this.gatewaySecret = gatewaySecret;
        this.internalApiKey = internalApiKey;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/actuator/health") || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        boolean internal = request.getRequestURI().startsWith("/internal/");
        boolean authenticated = internal
                ? internalApiKey.equals(request.getHeader("X-Internal-Api-Key"))
                : gatewaySecret.equals(request.getHeader("X-Gateway-Secret"))
                    && request.getHeader("X-User-Id") != null;
        if (!authenticated) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Service authentication required");
            return;
        }
        chain.doFilter(request, response);
    }
}
