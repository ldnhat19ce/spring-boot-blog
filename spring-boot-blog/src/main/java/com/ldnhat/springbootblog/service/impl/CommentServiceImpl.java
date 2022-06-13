package com.ldnhat.springbootblog.service.impl;

import com.ldnhat.springbootblog.dto.CommentDto;
import com.ldnhat.springbootblog.dto.PostDto;
import com.ldnhat.springbootblog.entity.CommentEntity;
import com.ldnhat.springbootblog.entity.PostEntity;
import com.ldnhat.springbootblog.exception.BlogAPIException;
import com.ldnhat.springbootblog.exception.ResourceNotFoundException;
import com.ldnhat.springbootblog.repository.CommentRepository;
import com.ldnhat.springbootblog.repository.PostRepository;
import com.ldnhat.springbootblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public CommentDto saveComment(Long postId, CommentDto commentDto) {
        CommentEntity commentEntity = mapToEntity(commentDto);

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));

        commentEntity.setPostEntity(postEntity);

        return mapToDto(commentRepository.save(commentEntity));
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<CommentEntity> commentEntities = commentRepository.findByPostEntityId(postId);
        return commentEntities.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    // (Parameters) -> { Body }
    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));

        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", commentId));

        if (!commentEntity.getPostEntity().getId().equals(postEntity.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
       return mapToDto(commentEntity);
    }

    @Override
    public CommentDto editComment(Long postId, long commentId, CommentDto commentRequest) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );

        // retrieve comment by id
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", commentId)
        );

        if (!commentEntity.getPostEntity().getId().equals(postEntity.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comment does not belong to post");
        }
        commentEntity.setName(commentRequest.getName());
        commentEntity.setEmail(commentRequest.getEmail());
        commentEntity.setBody(commentRequest.getBody());

        CommentEntity updatedComment = commentRepository.save(commentEntity);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );

        // retrieve comment by id
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", commentId)
        );

        if (!commentEntity.getPostEntity().getId().equals(postEntity.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comment does not belong to post");
        }

        commentRepository.delete(commentEntity);
    }

    private CommentDto mapToDto(CommentEntity commentEntity){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentEntity.getId());
        commentDto.setName(commentEntity.getName());
        commentDto.setEmail(commentEntity.getEmail());
        commentDto.setBody(commentEntity.getBody());

        return commentDto;
    }

    private CommentEntity mapToEntity(CommentDto commentDto){
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setName(commentDto.getName());
        commentEntity.setEmail(commentDto.getEmail());
        commentEntity.setBody(commentDto.getBody());

        return commentEntity;
    }
}
