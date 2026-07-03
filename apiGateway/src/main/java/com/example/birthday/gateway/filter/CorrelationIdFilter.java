package com.example.birthday.gateway.filter;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {
    public static final String HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        String finalId = correlationId;
        var request = exchange.getRequest().mutate().headers(headers -> {
            headers.set(HEADER, finalId);
        }).build();
        exchange.getResponse().getHeaders().set(HEADER, finalId);
        return chain.filter(exchange.mutate().request(request).build())
                .contextWrite(context -> context.put(HEADER, finalId))
                .doFirst(() -> MDC.put("correlationId", finalId))
                .doFinally(signal -> MDC.remove("correlationId"));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
