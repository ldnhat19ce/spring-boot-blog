package com.ldnhat.springbootblog.service;

import com.ldnhat.springbootblog.dto.PostDto;
import com.ldnhat.springbootblog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto savePost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(Long id);
    PostDto editPost(PostDto postDto, Long id);
    void deletePostById(Long id);
}
