package com.example.birthday.calendarservice.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter{
    private final String secret;
    public GatewayAuthenticationFilter(@Value("${security.gateway-secret}")String secret){this.secret=secret;}
    @Override protected boolean shouldNotFilter(HttpServletRequest r){String p=r.getRequestURI();return p.equals("/actuator/health")||p.startsWith("/v3/api-docs")||p.startsWith("/swagger-ui");}
    @Override protected void doFilterInternal(HttpServletRequest r,HttpServletResponse s,FilterChain c)throws ServletException,IOException{
        if(!secret.equals(r.getHeader("X-Gateway-Secret"))||r.getHeader("X-User-Id")==null){s.sendError(HttpStatus.UNAUTHORIZED.value(),"Gateway authentication required");return;}c.doFilter(r,s);
    }
}
