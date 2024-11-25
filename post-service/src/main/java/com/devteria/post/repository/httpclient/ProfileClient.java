package com.devteria.post.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.devteria.post.dto.ApiResponse;
import com.devteria.post.dto.response.UserProfileResponse;

@FeignClient(name = "profile-client", url = "http://localhost:8081/profile")
public interface ProfileClient {
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId);
}
