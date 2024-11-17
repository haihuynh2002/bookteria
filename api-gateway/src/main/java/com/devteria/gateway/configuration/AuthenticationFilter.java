package com.devteria.gateway.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @NonFinal
    public static String[] PUBLIC_POST_ENPOINTS = {"/identity/auth/.*", "/identity/users"};

    @NonFinal
    @Value("${app.api-prefix}") 
    String apiPrefix;

    IdentityService identityService;
    ObjectMapper objectMapper;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEnpoint(exchange.getRequest())) {
           return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(Objects.isNull(authHeaders) || CollectionUtils.isEmpty(authHeaders)) {
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeaders.getFirst().replace("Bearer", "");

        return identityService.introspect(token).flatMap(response -> {
            return response.getResult().isValid()
            ? chain.filter(exchange)
            : unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
        .code(1401)
        .message("unauthenticated")
        .build();
        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
            Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }

    private boolean isPublicEnpoint(ServerHttpRequest request) {
        return request.getMethod().equals(HttpMethod.POST)
        ? Arrays.stream(PUBLIC_POST_ENPOINTS).anyMatch(enpoint -> request.getURI().getPath().matches(apiPrefix + enpoint))
        : false;
    }
}
