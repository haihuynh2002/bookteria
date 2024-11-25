package com.devteria.profile.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createUserProfile(UserProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse getUserProfile(String id) {
        return userProfileMapper.toUserProfileResponse(
                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getUserProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    public UserProfileResponse getByUserId(String userId) {
        return userProfileMapper.toUserProfileResponse(
                userProfileRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("user not found")));
    }
}
