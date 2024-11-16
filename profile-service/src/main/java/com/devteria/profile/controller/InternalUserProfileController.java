package com.devteria.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/users")
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileResponse> createUserProfile(@RequestBody UserProfileCreationRequest request) {
        return ResponseEntity.ok().body(userProfileService.createUserProfile(request));
    }
}
