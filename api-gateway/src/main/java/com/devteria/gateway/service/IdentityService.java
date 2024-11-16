package com.devteria.gateway.service;

import org.springframework.stereotype.Service;

import com.devteria.gateway.dto.request.IntrospectRequest;
import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.dto.response.IntrospectResponse;
import com.devteria.gateway.repository.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class IdentityService {
    IdentityClient identityClient;


    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        IntrospectRequest request = IntrospectRequest.builder()
        .token(token)
        .build();
        return identityClient.introspect(request);
    }
}
