package com.example.birthday.gateway.filter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/actuator/health"
    );

    private final SecretKey key;
    private final String gatewaySecret;

    public JwtGatewayFilter(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.gateway-secret}") String gatewaySecret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.gatewaySecret = gatewaySecret;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (path.startsWith("/internal/")) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().setComplete();
        }
        if (PUBLIC_PATHS.contains(path)) {
            return chain.filter(exchange);
        }

        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }
        try {
            var claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(authorization.substring(7)).getPayload();
            var request = exchange.getRequest().mutate().headers(headers -> {
                headers.remove("X-User-Id");
                headers.remove("X-User-Email");
                headers.remove("X-User-Role");
                headers.set("X-User-Id", claims.getSubject());
                headers.set("X-User-Email", claims.get("email", String.class));
                headers.set("X-User-Role", claims.get("role", String.class));
                headers.remove("X-Gateway-Secret");
                headers.set("X-Gateway-Secret", gatewaySecret);
            }).build();
            return chain.filter(exchange.mutate().request(request).build());
        } catch (JwtException | IllegalArgumentException ex) {
            return unauthorized(exchange);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
