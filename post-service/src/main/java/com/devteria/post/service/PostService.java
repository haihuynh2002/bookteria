package com.devteria.post.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devteria.post.dto.ApiResponse;
import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PageResponse;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.dto.response.UserProfileResponse;
import com.devteria.post.entity.Post;
import com.devteria.post.mapper.PostMapper;
import com.devteria.post.repository.PostRepository;
import com.devteria.post.repository.httpclient.ProfileClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;
    ProfileClient profileClient;
    
    public PostResponse createPost(PostRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var post = Post.builder()
        .content(request.getContent())
        .userId(auth.getName())
        .createdDate(Instant.now())
        .modifiedDate(Instant.now())
        .build();

        return postMapper.toPostResponse(postRepository.save(post));
    }

    public PageResponse<PostResponse> getMyPosts(int page, int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = auth.getName();

        final UserProfileResponse userProfile = profileClient.getProfile(userId).getResult();

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> pageData = postRepository.findAllByUserId(userId, pageable);
        
        var postList = pageData.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
            postResponse.setUsername(userProfile.getUsername());
            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
        .currentPage(page)
        .pageSize(size)
        .totalPages(pageData.getTotalPages())
        .totalElements(pageData.getTotalElements())
        .data(postList)
        .build();
    }
}
