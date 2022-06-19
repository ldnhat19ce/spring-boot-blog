package com.ldnhat.springbootblog.controller;

import com.ldnhat.springbootblog.dto.CommentDto;
import com.ldnhat.springbootblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/v1/posts/{postId}/comments")
    public ResponseEntity<CommentDto> saveComment(
            @PathVariable(name = "postId") Long postId,
            @Valid @RequestBody CommentDto commentDto
    ){
        return new ResponseEntity<>(commentService.saveComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/v1/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
                                                     @PathVariable(value = "commentId") Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId, commentId);

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> editComment(@PathVariable(value = "postId") Long postId,
                                                  @PathVariable(value = "commentId") Long commentId,
                                                  @Valid @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.editComment(postId, commentId, commentDto);

        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                  @PathVariable(value = "commentId") Long commentId){
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
