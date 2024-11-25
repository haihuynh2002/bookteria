package com.devteria.post.mapper;

import org.mapstruct.Mapper;

import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest request);
    PostResponse toPostResponse(Post post);
}
