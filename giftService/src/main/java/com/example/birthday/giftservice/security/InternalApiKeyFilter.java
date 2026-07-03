package com.example.birthday.giftservice.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class InternalApiKeyFilter extends OncePerRequestFilter {
    private final String key;

    public InternalApiKeyFilter(@Value("${security.internal-api-key}") String key) {
        this.key = key;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/internal/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!key.equals(request.getHeader("X-Internal-Api-Key"))) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid internal API key");
            return;
        }
        chain.doFilter(request, response);
    }
}
