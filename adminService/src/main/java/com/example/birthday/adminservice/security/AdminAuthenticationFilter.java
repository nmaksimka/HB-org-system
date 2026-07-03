package com.example.birthday.adminservice.security;
import jakarta.servlet.*;import jakarta.servlet.http.*;import org.springframework.beans.factory.annotation.Value;import org.springframework.http.HttpStatus;import org.springframework.stereotype.Component;import org.springframework.web.filter.OncePerRequestFilter;import java.io.IOException;
@Component public class AdminAuthenticationFilter extends OncePerRequestFilter{
 private final String secret;public AdminAuthenticationFilter(@Value("${security.gateway-secret}")String s){secret=s;}
 @Override protected boolean shouldNotFilter(HttpServletRequest r){String p=r.getRequestURI();return p.equals("/actuator/health")||p.startsWith("/v3/api-docs")||p.startsWith("/swagger-ui");}
 @Override protected void doFilterInternal(HttpServletRequest r,HttpServletResponse s,FilterChain c)throws ServletException,IOException{
  if(!secret.equals(r.getHeader("X-Gateway-Secret"))||!"ADMIN".equals(r.getHeader("X-User-Role"))){s.sendError(HttpStatus.FORBIDDEN.value(),"Administrator role required");return;}c.doFilter(r,s);
 }
}
