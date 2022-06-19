package com.ldnhat.springbootblog.controller;

import com.ldnhat.springbootblog.dto.PostDto;
import com.ldnhat.springbootblog.payload.PostResponse;
import com.ldnhat.springbootblog.service.PostService;
import com.ldnhat.springbootblog.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(value = "crud api")
@RestController
@RequestMapping("/api")

public class PostController {

    @Autowired
    private PostService postService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/v1/posts")
    public ResponseEntity<PostDto> savePost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.savePost(postDto), HttpStatus.CREATED);
    }

    @GetMapping("/v1/posts")
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE
                    , required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)
                    String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
            ){
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }


    @GetMapping("/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostByIdV2(@PathVariable(name = "id") Long id){

        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/v1/posts/{id}")
    public ResponseEntity<PostDto> editPost(@Valid @RequestBody PostDto postDto,
                                            @PathVariable(name = "id") Long id){
        PostDto postResponse = postService.editPost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id){
        postService.deletePostById(id);

        return new ResponseEntity<>("Post Entity deleted successfully", HttpStatus.OK);
    }

}
