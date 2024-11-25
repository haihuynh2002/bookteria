package com.devteria.post.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.entity.Post;
import com.devteria.post.mapper.PostMapper;
import com.devteria.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    
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

    public List<PostResponse> getMyPosts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = auth.getName();
        return postRepository.findAllByUserId(userId).stream().map(postMapper::toPostResponse).toList();
    }
}
