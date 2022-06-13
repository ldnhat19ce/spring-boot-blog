package com.ldnhat.springbootblog.service;

import com.ldnhat.springbootblog.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto saveComment(Long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto editComment(Long postId, long commentId, CommentDto commentRequest);

    void deleteComment(Long postId, Long commentId);
}
